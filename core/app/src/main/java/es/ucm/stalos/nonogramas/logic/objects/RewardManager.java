package es.ucm.stalos.nonogramas.logic.objects;

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
    public RewardManager(Engine engine, GameState gameState) {
        _engine = engine;
        _gameState = gameState;

        loadAd();
    }

    private void loadAd() {
        _engine.getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_rewardedAd == null) {
                    _isLoading = true;
                    AdRequest adRequest = new AdRequest.Builder().build();
                    RewardedAd.load(_engine.getContext(),
                            "ca-app-pub-3940256099942544/5224354917",
                            adRequest, new RewardedAdLoadCallback() {
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
        });
    }

    private void setFullScreen() {
        _engine.getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void showAd() {
        _engine.getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_rewardedAd != null) {
                    _rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            System.out.println("Ad dismissed fullscreen");
                            _rewardedAd = null;
                            // Preload the next ad
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
                            System.out.println("RECOMPENSAAAA:");
                            // TODO: Devolver vidas
                            _gameState.restoreLives();
                        }
                    });
                } else {
                    System.out.println("Recompensa no disponible");
                }
            }
        });

    }

    private Engine _engine;
    private GameState _gameState;
    private RewardedAd _rewardedAd;
    private boolean _isLoading;
}
