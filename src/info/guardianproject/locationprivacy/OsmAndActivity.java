
package info.guardianproject.locationprivacy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import net.osmand.util.GeoPointParserUtil;
import net.osmand.util.GeoPointParserUtil.GeoParsedPoint;

public class OsmAndActivity extends Activity {
    public static final String TAG = "OsmAndActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        Log.i(TAG, "intent action " + intent.getAction());
        String categories = "";
        if (intent.getCategories() != null)
            for (String category : intent.getCategories())
                categories += " " + category;
        Log.i(TAG, "intent categories " + categories);

        Uri uri = intent.getData();
        Log.i(TAG, "uri: " + uri);
        if (uri != null) {
            GeoParsedPoint point = GeoPointParserUtil.parse(uri.toString());
            if (point == null) {
                // can't do anything else, try to force HTTPS
                String host = uri.getHost();
                if (host.startsWith("maps.yandex.")
                        || host.equals("here.com")
                        || host.endsWith(".here.com")) {
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https");
                    intent.setData(builder.build());
                }
            } else {
                intent.setData(Uri.parse(point.toString()));
            }
        }

        Log.i(TAG, "startActivity uri: " + intent.getData());
        intent.setComponent(null); // prompt user for app to view new URI
        startActivity(intent);
        finish();
    }
}
