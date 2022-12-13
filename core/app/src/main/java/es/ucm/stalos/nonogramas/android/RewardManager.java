package es.ucm.stalos.nonogramas.android;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.logic.states.GameState;

public class RewardManager {
    public RewardManager(GameState gameState) {
        _gameState = gameState;
    }

    /**
     * If there isn't ad loaded, loads a rewarded ad based on
     * the ad ID of the current app.
     *
     * @param context the current app context
     */
    public void loadAd(Activity context) {
        if (_rewardedAd == null) {
            _isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(context, "ca-app-pub-3940256099942544/5224354917", adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    _rewardedAd = null;
                    System.out.println("Rewarded Ad failed to load");
                    _isLoading = false;
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    _rewardedAd = rewardedAd;
                    System.out.println("Rewarded Ad was loaded");
                    _isLoading = false;
                }
            });
        }
    }

    /**
     * If there is an ad loaded shows the ad fullscreen
     *
     * @param context the app context where to show the ad
     */
    public void showAd(Activity context) {
        if (_rewardedAd != null) {
            _rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    System.out.println("Ad dismissed fullscreen");
                    _rewardedAd = null;
                    // Preload the next ad
                    loadAd(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    System.out.println("Ad failed to show fullscreen");
                    _rewardedAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    System.out.println("Ad showed fullscreen");
                    // TODO: para el cambio de landscape mientra se reproduce anuncio
                }
            });

            _rewardedAd.show(context, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle reward
                    _gameState.restoreLives();
                }
            });
        } else {
            System.out.println("Recompensa no disponible");
            _gameState.restoreLives();
        }
    }


    private GameState _gameState;
    private RewardedAd _rewardedAd;
    private boolean _isLoading;
}
