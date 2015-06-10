package me.toxz.squarethumbnailvideoview.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Created by yyz on 6/9/15.
 */
public final class SquareThumbnailVideoView extends FrameLayout implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "SquareThumbnailVideoV";

    public SquareThumbnailVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SquareThumbnailVideoView(Context context) {
        super(context);
        init(null, 0);
    }

    public SquareThumbnailVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareThumbnailVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(
                getMeasuredWidth(),
                getMeasuredHeight()
        );

        Log.i(TAG, "size: " + size);
        setMeasuredDimension(size, size);
    }

    private VideoView mVideoView;
    private ImageView mThumbImageView;
    private ImageButton mControlButton;
    private Bitmap mThumbnailBitmap = null;

    private Drawable mPlayButtonDrawable = getResources().getDrawable(R.drawable.stvv_defalut_play_button);
    private boolean isFullFrameControl = true;

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.stvv_SquareThumbnailVideoView, defStyleAttr, 0);

        isFullFrameControl = a.getBoolean(R.styleable.stvv_SquareThumbnailVideoView_stvv_isPlayButtonFullFrameControl, isFullFrameControl);

        if (a.hasValue(R.styleable.stvv_SquareThumbnailVideoView_stvv_playButtonImage)) {
            mPlayButtonDrawable = a.getDrawable(R.styleable.stvv_SquareThumbnailVideoView_stvv_playButtonImage);
            assert mPlayButtonDrawable != null;
            mPlayButtonDrawable.setCallback(this);
        }
        a.recycle();

        initView();
    }

    private void initView() {
        mVideoView = new VideoView(getContext());
        mThumbImageView = new ImageView(getContext());
        mControlButton = new ImageButton(getContext());

        this.addView(mVideoView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.addView(mThumbImageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (mThumbnailBitmap != null) {
            mThumbImageView.setImageBitmap(mThumbnailBitmap);
            mThumbImageView.setVisibility(VISIBLE);
        } else {
            mThumbImageView.setVisibility(INVISIBLE);
        }


        mControlButton.setImageDrawable(mPlayButtonDrawable);
        mControlButton.setScaleType(ImageView.ScaleType.CENTER);
        if (Build.VERSION.SDK_INT > 16) mControlButton.setBackground(null);
        else mControlButton.setBackgroundResource(0);

        LayoutParams buttonParams;
        if (isFullFrameControl) {
            buttonParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            buttonParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        buttonParams.gravity = Gravity.CENTER;
        this.addView(mControlButton, buttonParams);

        mControlButton.setOnClickListener(this);
    }

    /**
     * set res id of the thumbnail
     */
    public void setThumbnailBitmap(Bitmap bitmap) {
        if (mThumbImageView == null) {
            mThumbnailBitmap = bitmap;
        } else {
            mThumbnailBitmap = bitmap;
            mThumbImageView.setImageBitmap(bitmap);
            mThumbImageView.setVisibility(VISIBLE);
        }
    }

    // Delegate VideoView
    public void setVideoURI(Uri uri) {
        mVideoView.setVideoURI(uri);
    }

    public void seekTo(int msec) {
        mVideoView.seekTo(msec);
    }

    public void stopPlayback() {
        mVideoView.stopPlayback();
    }

    public void setMediaController(MediaController controller) {
        mVideoView.setMediaController(controller);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        mVideoView.setOnPreparedListener(l);
    }

    public boolean canSeekForward() {
        return mVideoView.canSeekForward();
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener l) {
        mVideoView.setOnErrorListener(l);
    }

    public int getDuration() {
        return mVideoView.getDuration();
    }

    /**
     * Play single Video.
     *
     * @param path
     */
    public void setVideoPath(final String path) {
        mAdapter = new VideoAdapter<String>() {

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public String getItem(int position) {
                return path;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public String getVideoPath(int position) {
                return path;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }

    private VideoAdapter mAdapter;

    public void setVideoAdapter(VideoAdapter adapter) {
        mAdapter = adapter;
    }

    public void resume() {
        mVideoView.resume();
    }

    public void suspend() {
        mVideoView.suspend();
    }

    public int getBufferPercentage() {
        return mVideoView.getBufferPercentage();
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        mVideoView.setOnCompletionListener(l);
    }

    public void pause() {
        mVideoView.pause();
    }

    public SurfaceHolder getHolder() {
        return mVideoView.getHolder();
    }

    public boolean canPause() {
        return mVideoView.canPause();
    }


    private int currentVideoIndex = -1;

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        mVideoView.setZOrderMediaOverlay(isMediaOverlay);
    }

    public void setZOrderOnTop(boolean onTop) {
        mVideoView.setZOrderOnTop(onTop);
    }

    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    public boolean canSeekBackward() {
        return mVideoView.canSeekBackward();
    }

    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }
    // Delegate VideoView

    @Override
    public void onClick(View v) {
        this.start();
    }


    /**
     * play the video or video in adapter.
     */
    private void start() {
        if (mAdapter == null) {
            throw new IllegalStateException("Please set video or video adapter before start");
        }
        if (Build.VERSION.SDK_INT >= 17) {
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @SuppressLint("NewApi")
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        mVideoView.setVisibility(View.GONE);
                    }
                    mVideoView.setOnInfoListener(null);
                    return true;
                }
            });
        } else {
            mThumbImageView.setVisibility(View.GONE);
        }
        mControlButton.setVisibility(INVISIBLE);

    }

    private void playVideo() {
        if (currentVideoIndex++ < mAdapter.getCount()) {
            String path = mAdapter.getVideoPath(currentVideoIndex);
            mVideoView.setVideoPath(path);
            mVideoView.setOnCompletionListener(this);
            mVideoView.start();
        } else {
            currentVideoIndex = -1;
            if (mThumbImageView.getDrawable() != null) {
                mThumbImageView.setVisibility(VISIBLE);
            }
            mControlButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playVideo();
    }


}
