/**
 *  SafeMonit App
 *
 *  Copyright 2017 Alan Graham
 *
 */

definition(
    name: "SafeMonit App",
    namespace: "safemonit",
    author: "Alan Graham",
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
  subscribe(location, "mode", locationChangeHandler)
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
    date: ev.date,
  ]
  notifySafeMonit(data, false);
}

def locationChangeHandler(ev) {
  def data = [
    event_type: "mode.changed",
    value: ev.value
  ]
  notifySafeMonit(data, false);
}

def notifySafeMonit(data, isFull) {
  prepareResponseData(data)
  def params = [
    uri: "http://dishfave.com:8080/api/location_state/report",
    body: [
      event: data
    ]
  ]
  httpPostJson(params) { resp ->
    log.debug(resp.data)
    if (!isFull) {
      log.debug("About to notify full state")
      notifyFullLocationState()
    }
  }
}

/// HELPERS

def prepareResponseData(data) {
  data['location_api_key'] = locationAPIKey
  data['app_url'] = apiServerUrl("/api/smartapps/installations/${app.id}")
  data['app_access_token'] = state.accessToken
  data['app_id'] = app.id
  data['platform'] = "SmartThings"
}

def renderLocationState() {
  log.debug("Notify full state")
  def data = [
    location: renderLocation(),
    devices: renderDevices()
  ]
  prepareResponseData(data)
  return data
}

def renderLocation() {
  [
    latitude: location.latitude,
    longitude: location.longitude,
    mode: location.mode,
    name: location.name,
    temperature_scale: location.temperatureScale,
    zip_code: location.zipCode,
    hub_ip: location.hubs[0].localIP,
    smartapp_version: '1.0.0'
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
  try {
    [
      id: dev.id,
      name: dev.displayName,
      basename: dev.name,
      status: dev.status,
      manufacturer_name: dev.getManufacturerName(),
      model_name: dev.getModelName(),
      last_time: dev.getLastActivity(),
      capabilities: renderDeviceCapabilities(dev),
      commands: renderDeviceCommands(dev),
      attributes: renderDeviceAttributes(dev)
    ]
  } catch (err) {
    log.debug("Could not parse device ${dev.id}");
    log.debug(err.getMessage());
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
    def val = null
    try {
      val = dev.currentValue(e.name)
    } catch (ex) { }
    [
      (e.name): val
    ]
  }
}
