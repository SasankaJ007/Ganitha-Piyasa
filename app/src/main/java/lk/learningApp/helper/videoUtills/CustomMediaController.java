package lk.learningApp.helper.videoUtills;

import android.content.Context;
import android.widget.MediaController;

public class CustomMediaController extends MediaController {
    public CustomMediaController(Context context) {
        super(context);
    }

    // Override the hide method to prevent auto-hiding
    @Override
    public void hide() {
        // Don't hide the controller
    }
}
