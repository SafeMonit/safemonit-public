/**
 *  SafeMonit App
 *
 *  Copyright 2017 SafeMonit
 *
 */

definition(
    name: "SafeMonit App",
    namespace: "safemonit",
    author: "SafeMonit",
    description: "API for SafeMonit with complete set of devices",
    category: "SmartThings Labs",
    iconUrl:   "https://raw.githubusercontent.com/pdlove/homebridge-smartthings/master/smartapps/JSON%401.png",
    iconX2Url: "https://raw.githubusercontent.com/pdlove/homebridge-smartthings/master/smartapps/JSON%402.png",
    iconX3Url: "https://raw.githubusercontent.com/pdlove/homebridge-smartthings/master/smartapps/JSON%403.png",
    oauth: true)


preferences {
    section("Select all devices to monitor below.") {
      input "sensorList", "capability.sensor", title: "Sensors", multiple: true, required: false
      input "switchList", "capability.switch", title: "Switches", multiple: true, required: false
      input "deviceList", "capability.refresh", title: "Any Others", multiple: true, required: false
    }

    section() {
      input "locationAPIKey", "text", title: "Location API Key", multiple: false, required: true
      paragraph "The Location API key can be found on your location details page at safemonit.com."
    }
}

mappings {
  path("/location_state") { action: [GET: "renderLocationState"] }
  path("/location_events") { action: [GET: "renderEvents"] }
  path("/command") { action: [POST: "issueCommand"] }
}

def installed() {
  initialize()
}

def updated() {
  unsubscribe()
  initialize()
}

def initialize() {
  if (!state.accessToken) {
    createAccessToken()
  }
  registerDevices()
  subscribe(location, "mode", locationModeChangeHandler)
  subscribe(location, "routineExecuted", locationRoutineExecutedHandler)
  // send initial data
  def data = [
    event_type: "mode.changed",
    value: location.mode
  ]
  notifySafeMonit(data, true);
}

def registerDevices() {
  //This has to be done at startup because it takes too long for a normal command.
  log.debug "Registering All Events"
  state.devchanges = []
  registerChangeHandler(deviceList)
  registerChangeHandler(sensorList)
  registerChangeHandler(switchList)
}

def registerChangeHandler(myList) {
  myList.each { myDevice ->
    def theAtts = myDevice.supportedAttributes
    theAtts.each {att ->
      subscribe(myDevice, att.name, deviceChangeHandler)
      log.debug "Registering ${myDevice.displayName}.${att.name}"
    }
  }
}

def deviceChangeHandler(ev) {
  // send to SafeMonit
  def data = [
    event_type: "device.changed",
    device_id: ev.deviceId,
    attribute: ev.name,
    value: ev.value,
    time: ev.isoDate,
    change: ev.isStateChange(),
  ]
  notifySafeMonit(data, false);
}

def locationModeChangeHandler(ev) {
  def data = [
    event_type: "mode.changed",
    value: ev.value
  ]
  notifySafeMonit(data, false);
}

def locationRoutineExecutedHandler(ev) {
  def data = [
    event_type: "routine.executed",
    smartapp_id: ev.value,
    name: ev.displayName,
    description: ev.descriptionText
  ]
  notifySafeMonit(data, false);
}

def notifySafeMonit(data, isFull) {
  prepareResponseData(data)
  def uri = "https://api.safemonit.com/v1/location_state/report"
  if (data["test_api_mode"] == true) {
    uri = "http://api.safemonit.com:4019/v1/location_state/report"
  }
  def params = [
    uri: uri,
    body: [
      event: data
    ]
  ]
  httpPostJson(params) { resp ->
    //log.debug(resp.data)
  }
}

def issueCommand() {
  log.debug("Received request to issue command.")
  //log.debug(request.JSON)
  def context = request.JSON?.context
  def command = request.JSON?.command
  def value = request.JSON?.value
  if (context == "location") {
    if (command == "setMode") {
      location.setMode(value)
      log.debug("Setting location mode to ${value}")
    } else if (command == "sendPush") {
      sendPush(value)
    } else if (command == "executeRoutine") {
      location.helloHome?.execute(value)
    }
  } else if (context == "device") {
    def did = request.JSON?.device_id
    def device = findDeviceById(did)
    device."$command"()
    log.debug("Running device ${did} command '${command}'")
  }
  render data: new groovy.json.JsonOutput().toJson([success: true])
}

/// HELPERS

def findDeviceById(id) {
  def device = deviceList.find { it.id == id }
  if (device) return device
  device = sensorList.find { it.id == id }
  if (device) return device
  device = switchList.find { it.id == id }
  return device
}

def prepareResponseData(data) {
  if (locationAPIKey != null && locationAPIKey[0] == "!") {
    data['location_api_key'] = locationAPIKey.substring(1)
    data['test_api_mode'] = true
  } else {
    data['location_api_key'] = locationAPIKey
  }
  data['app_url'] = apiServerUrl("/api/smartapps/installations/${app.id}")
  data['app_access_token'] = state.accessToken
  data['app_id'] = app.id
  data['platform'] = "SmartThings"
}

def renderLocationState() {
  log.debug("Notify full state")
  def res = [
    success: true
  ]
  def status = 200
  try {
    res['location'] = renderLocation()
    res['devices'] = renderDevices()
  } catch (e) {
    res['success'] = false
    res['error'] = "An error occurred building location state."
    status = 500
    log.error("Exception caught", e)
  }
  prepareResponseData(res)
  render data: new groovy.json.JsonOutput().toJson(res), status: status
}

def renderLocation() {
  log.debug "Rendering location"
  [
    uuid: location.id,
    latitude: location.latitude,
    longitude: location.longitude,
    mode: location.mode,
    modes: location.modes.collect { e-> 
      e.name
    },
    routines: location.helloHome?.getPhrases()*.label,
    name: location.name,
    temperature_scale: location.temperatureScale,
    zip_code: location.zipCode,
    hub_ip: location.hubs[0].localIP,
    smartapp_version: '1.2.4'
  ]
}

def renderDevices() {
  def data = []
  sensorList.each { data << renderDevice(it) }
  switchList.each { data << renderDevice(it) }
  deviceList.each { data << renderDevice(it) }
  return data.findAll { it != null }
}

def renderDevice(dev) {
  log.debug "Rendering device ${dev.id}"
  try {
    [
      id: dev.id,
      name: dev.displayName,
      basename: dev.name,
      status: dev.status,
      manufacturer_name: dev.getManufacturerName(),
      model_name: dev.getModelName(),
      //last_time: dev.getLastActivity(),
      capabilities: renderDeviceCapabilities(dev),
      commands: renderDeviceCommands(dev),
      attributes: renderDeviceAttributes(dev)
    ]
  } catch (err) {
    log.error("Could not parse device ${dev.id}", err);
    return null
  }
}

def renderDeviceCapabilities(dev) {
  //log.debug "Rendering device capabilities"
  dev.capabilities.collectEntries { e->
    [
      (e.name): true
    ]
  }
}
def renderDeviceCommands(dev) {
  //log.debug "Rendering device commands"
  dev.supportedCommands.collectEntries { e->
    [
      (e.name): (e.arguments)
    ]
  }
}
def renderDeviceAttributes(dev) {
  //log.debug "Rendering device attributes"
  dev.supportedAttributes.collectEntries { e->
    def ret = null
    def val = null
    try {
      val = dev.currentValue(e.name)
      if (val != null) {
        def s = dev.currentState(e.name)
        ret = [
          value: val
        ]
        if (s.unit != null) { ret['unit'] = s.unit }
        if (s.isoDate != null) { ret['time'] = s.isoDate }
      }
    } catch (ex) { }
    [
      (e.name): ret
    ]
  }
}

def renderEvents() {
  log.debug("Render events")
  def res = [
    success: true
  ]
  def status = 200
  try {
    // find events for device
    def data = []
    def did = request.JSON?.device_id
    def device = findDeviceById(did)
    def evs = device.eventsSince(new Date() - 1, [max: 100])
    evs.each { data << renderEvent(it) }
    data = data.findAll { it != null }
    res['events'] = data
  } catch (e) {
    res['success'] = false
    res['error'] = "An error occurred building events."
    status = 500
    log.error("Exception caught", e)
  }
  //prepareResponseData(res)
  render data: new groovy.json.JsonOutput().toJson(res), status: status

}

def renderEvent(ev) {
  def ret = [
    time: ev.isoDate,
    description: ev.descriptionText,
    displayName: ev.displayName,
    value: ev.value,
    device_id: ev.deviceId,
    name: ev.name,
    unit: ev.unit,
    change: ev.isStateChange(),
  ]
  return ret
}
