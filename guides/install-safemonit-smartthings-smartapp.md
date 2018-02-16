# How To Install SafeMonit Smart App

In order to integrate your SmartThings system with SafeMonit, you will need to first install the SmartApp. If you haven't installed a custom SmartApp before, it's okay, we will take you step by step.

## Add SafeMonit To Your Available SmartApps

1. First, you will want to navigate your web browser to <https://graph.api.smartthings.com>.

2. From there, click __My Locations__ in the top header, and then click the name of the Location you want to integrate with SafeMonit.

3. Click __My SmartApps__ in the top header and click the green __+ New SmartApp__ button.

4. Choose the __From Code__ tab and copy and paste the code from <https://raw.githubusercontent.com/SafeMonit/safemonit-public/master/smartapps/safemonit-app.groovy>. Then click the __Create__ button at the bottom.

5. Select the option to __Publish__ the SmartApp.

6. In the SmartApp settings page, select the option to __Enable OAuth2__. Now you are ready to enable the smart app from the official SmartThings app.


## Configure SafeMonit SmartApp

1. Open the __SmartThings__ app on your iOS and Android device.

2. Select the __Automation__ tab and select __SmartApps__.

3. Tap __Add A SmartApp__. Scroll down to the bottom and tap __My Apps__. Select the __SafeMonit App__.

4. Configure the app by entering the information needed. Select the sensors you want to monitor, and enter the __Location API Key__. The __Location API Key__ can be found on the location overview page in the __Location Details__ section.

5. To confirm your setup, trigger one of your selected sensors. This will send data to SafeMonit and initialize the connection.

That's it, you should now be able to configure panels and automation!

## Need Help?

If you have any issues during this setup, please contact us at [contact@safemonit.com](mailto:contact@safemonit.com).
