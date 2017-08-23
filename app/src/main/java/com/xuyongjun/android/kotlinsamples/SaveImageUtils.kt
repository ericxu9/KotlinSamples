package com.xuyongjun.android.kotlinsamples

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import io.reactivex.*
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

/**
 * Created by xuyongjun on 2017/8/23.
 */
object SaveImageUtils {
    public fun saveImage(context: Context, imageUrl: String, title: String): Observable<String> {
        return Observable.create(object : ObservableOnSubscribe<Bitmap> {
            override fun subscribe(e: ObservableEmitter<Bitmap>) {
                val bitmap = Glide.with(context)
                        .load(imageUrl)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                if (bitmap == null) {
                    e.onError(Exception("图片不存在"))
                } else {
                    e.onNext(bitmap)
                }
                e.onComplete()
            }
        }).flatMap { t ->
            val imageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath)
            val imagePath = File(imageDir, title.replace("/","-")+".jpg")
            if (imagePath.exists())
                imagePath.mkdir()

            try {
                val fos = FileOutputStream(imagePath)
                t.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var uri: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", imagePath)
            } else {
                uri = Uri.parse(imagePath.absolutePath)
            }
            //通知系统刷新相册
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
            context.sendBroadcast(intent)

            Observable.just(imagePath.absolutePath)
        }.subscribeOn(Schedulers.io())
    }
}