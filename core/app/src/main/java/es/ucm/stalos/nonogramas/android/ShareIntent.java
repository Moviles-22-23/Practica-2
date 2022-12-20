package es.ucm.stalos.nonogramas.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import es.ucm.stalos.nonogramas.logic.enums.ShareType;

public class ShareIntent {
    /**
     * Create a new intent to be shared
     * Always use string resources for UI text
     *
     * @param msg msg to display
     */
    public ShareIntent(String msg) {
        _sendIntent = new Intent();
        _msg = msg;
    }

    /**
     * Share the current Intent into the context activity
     *
     * @param context A Context for on-demand initialization.
     */
    public void shareContent(Context context, ShareType type) {
        String url = "";
        switch (type)
        {
            case TWITTER:
                url = "https://twitter.com/intent/tweet";
                _msg = _msg + "\nhttps://github.com/Moviles-22-23/Practica-2 \uD83D\uDE0A";
                break;
            case WHATSAPP:
                url = "https://api.whatsapp.com/send";
                _msg = _msg + "\nhttps://github.com/Moviles-22-23/Practica-2 \uD83D\uDE0A";
                break;
        }

        Uri builtURI = Uri.parse(url).buildUpon()
                .appendQueryParameter("text", _msg)
                .build();

        _sendIntent.setAction(Intent.ACTION_VIEW);
        _sendIntent.setData(builtURI);

        // Verify the original intent will resolve to at least one activity
        if (_sendIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(_sendIntent);
        }
    }

    private Intent _sendIntent;
    private String _msg;
}
