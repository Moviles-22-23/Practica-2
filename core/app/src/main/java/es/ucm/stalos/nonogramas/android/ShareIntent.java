package es.ucm.stalos.nonogramas.android;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ThreadLocalRandom;

import es.ucm.stalos.nonogramas.logic.enums.ShareType;

public class ShareIntent {
    /**
     * Create a new intent to be shared
     * Always use string resources for UI text
     *
     * @param title title of the intent
     */
    public ShareIntent(String title) {
        _sendIntent = new Intent(title);
    }

    /**
     * Share the current Intent into the context activity
     *
     * @param context AppCompatActivity of the which will use the intent
     */
    public void shareContent(AppCompatActivity context, ShareType type) {
        String url = "";
        switch (type)
        {
            case TWITTER:
                url = "https://twitter.com/intent/tweet";
                break;
            case WHATSAPP:
                url = "https://api.whatsapp.com/send";
                break;
            case TELEGRAM:
                url = "http://telegram.me/send";
                break;
        }

        int rnd = ThreadLocalRandom.current().nextInt(0, _msgs.length);
        String msg = _msgs[rnd];

        Uri builtURI = Uri.parse(url).buildUpon()
                .appendQueryParameter("text", msg)
                .build();

        _sendIntent.setAction(Intent.ACTION_VIEW);
        _sendIntent.setData(builtURI);

        // Verify the original intent will resolve to at least one activity
        if (_sendIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(_sendIntent);
        }
    }

    private Intent _sendIntent;
    // TODO: Poner otros mensajes
    private String[] _msgs =
            {
                    "Te echamos de menos. Nuevos retos te esperan \uD83D\uDE0A",
                    "No pierdas más el tiempo. Enfréntate a la aventura \uD83D\uDE00",
                    "Parece que llevas un tiempo sin jugar. " +
                            "Es hora de ejercitar un poco el coco \uD83D\uDE00"
            };
}
