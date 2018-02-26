# How To Configure Weather Tile

One of the most useful additions to the SafeMonit panels is the ability to show your local weather. This is best accommodated by using a virtual device setup within your SmartThings hub. If you don't already have it setup, this guide will show you how to do so.

## Add SmartWeather Station Tile

1. First, you will want to navigate your web browser to <https://graph.api.smartthings.com>.

2. From there, click __My Locations__ in the top header, and then click the name of the Location you have integrated with SafeMonit.

3. Next choose __My Devices__ and click __New Device__.

4. Enter the following options:

    | Name | Value |
    |------|-------|
    | __Name__ | Weather |
    | __Device Network Id__ | weather |
    |__Type__ | SmartWeather Station Tile |
    | __Version__ | Published |
    | __Location__ | [select your location] |
    | __Hub__ | [select your hub] |

5. You're all set. Now refresh your SafeMonit panel and enter manage mode to add a new tile, and select the 'Weather' mode tile. You should now see the weather device available to be selected.
