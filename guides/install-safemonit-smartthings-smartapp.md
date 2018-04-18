
In order to integrate your SmartThings system with SafeMonit, you will need to first install the SmartApp. If you haven't installed a custom SmartApp before, it's okay, we will take you step by step.

## Add SafeMonit To Your Available SmartApps

1. First, navigate your web browser to <https://graph.api.smartthings.com> and sign in with your SmartThings login and password.

2. From there, click __My Locations__ in the top header, and then click the name of the Location you want to integrate with SafeMonit.

3. Click __My SmartApps__ in the top header and click the green __+ New SmartApp__ button.

4. Choose the __From Code__ tab and copy and paste the code from <https://safemonit.com/download/smartapp>. Then click the __Create__ button at the bottom.

5. Click the __Publish__ button in the toolbar and select the __For Me__ option.

6. Click the __App Settings__ button in the toolbar. On this settings page, expand the __OAuth__ panel. Click the __Enable OAuth in Smart App__ button. Then click the __Update__ button at the bottom of the page. Now you are ready to enable the smart app from the official SmartThings app.


## Configure SafeMonit SmartApp

1. Open the __SmartThings__ app on your iOS and Android device.

2. Select the __Automation__ tab and select __SmartApps__.

3. Tap __Add A SmartApp__. Scroll down to the bottom and tap __My Apps__. Select the __SafeMonit App__.

4. Configure the app by entering the information needed. Select the sensors you want to monitor, and enter the __Location API Key__. The __Location API Key__ can be found on the location overview page in the __Location Details__ section.

5. To confirm your setup, trigger one of your selected sensors. This will send data to SafeMonit and initialize the connection.

That's it, you should now be able to configure panels and automation!


## Updating SafeMonit SmartApp

Periodically, changes may be pushed to the SmartApp to support new SafeMonit features. You will usually be notified with an alert in the location overview page that a new version is available.

1. First, navigate your web browser to <https://graph.api.smartthings.com> and sign in with your SmartThings login and password.

2. From there, click __My Locations__ in the top header, and then click the name of the Location you want to integrate with SafeMonit.

3. Click __My SmartApps__ in the top header and click the __safemonit : SafeMonit App__ link.

4. Delete all the existing code in the editor. Then copy and paste the code from <https://safemonit.com/download/smartapp>.

5. Click the __Save Button__. Then click the __Publish__ button in the toolbar and select the __For Me__ option.

6. Trigger a sensor (e.g. open a monitored door) to have the new SmartApp notify SafeMonit that it has been updated.

You are now running the latest SafeMonit SmartApp version.

## Need Help?

If you have any issues during this setup, please contact us at [support@safemonit.com](mailto:support@safemonit.com).
