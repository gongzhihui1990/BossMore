package org.gong.bmw.view;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.gtr.framework.rx.ProgressObserverImplementation;
import net.gtr.framework.rx.RxHelper;
import net.gtr.framework.util.Loger;

import org.gong.bmw.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * @author caroline
 * @date 2018/6/15
 */
public final class SplashActivity extends BaseActivity {

    @BindView(R.id.ivAppIcon)
    View ivAppIcon;
    boolean jump = false;

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void onViewLayout() {
        ivAppIcon.setOnClickListener(v -> {
            jump = true;
            splashEnd();
        });
        StatusBarUtil.setColor(this, getResources().getColor(android.R.color.transparent));
        final RxPermissions rxPermission = new RxPermissions(this);
        Observable<Boolean> initTask = Observable.just(true).delay(1, TimeUnit.SECONDS);
        Observable<Boolean> permissionTask = rxPermission.request(
                //蓝牙
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                //电话通讯录
                Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE,
                //位置
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                //相机、麦克风
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.CAMERA,
                //存储空间
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS).
                map(aBoolean -> {
                    Loger.d("权限结果 " + aBoolean);
                    return aBoolean;
                });
        Observable<Boolean> delayTask = Observable.just(true).delay(500, TimeUnit.MILLISECONDS);

        Observable<Boolean> splashObservable = Observable.zip(permissionTask, delayTask, initTask, (permission, delay, init) -> permission && delay && init);

        Observer<Boolean> splashObserver = new ProgressObserverImplementation<Boolean>(this) {
            @Override
            public void onNext(Boolean success) {
                super.onNext(success);
                if (!jump) {
                    splashEnd();
                }
            }
        }.setShow(false);
        RxHelper.bindOnUI(splashObservable, splashObserver);
    }

    private void splashEnd() {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

}
