package es.ucm.stalos.nonogramas.logic.objects;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.rewarded.OnAdMetadataChangedListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;

import es.ucm.stalos.androidengine.Engine;

public class RewardManager {
    public RewardManager(Engine engine) {
        _engine = engine;
        _adRequest = new AdRequest.Builder().build();
    }

    public void loadAd() {
        RewardedAd.load(_engine.getContext(), "ca-app-pub-3940256099942544/5224354917",
                _adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        _rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        _rewardedAd = rewardedAd;
                        System.out.println("Rewarded Ad was loaded");
                    }
                });
    }

    public void showAd() {
        if (_rewardedAd != null) {

            _rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    System.out.println("Ad dismissed fullscreen");
                    _rewardedAd = null;
                    // Preload next ad
                    loadAd();
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
                }
            });

            _rewardedAd.show(_engine.getContext(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle reward
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            System.out.println("NO HAY REWARD");
        }
    }

    private Engine _engine;
    private RewardedAd _rewardedAd;
    private AdRequest _adRequest;
}
