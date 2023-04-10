package com.udacity

import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.Timer
import kotlin.concurrent.schedule



class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var loadingRec: LoadingCircle
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var q: DownloadManager
    private lateinit var a: String
    private lateinit var c: String
    private lateinit var animator0: TheAnimation
    private lateinit var animator1: DTextAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingRec = findViewById(R.id.loading_rec)

        createChannel()

        q = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        a = "test2"
        c = "test4"


        animator1 = DTextAnimation(loadingRec)

        animator1.repeatCount = ObjectAnimator.INFINITE

        loadingRec.setOnClickListener {

            loadingRec.isClickable = false

            animator0 = TheAnimation(loadingRec, 360f, loadingRec.width.toFloat())

                animator0.duration = 3000
                animator0.repeatCount = ObjectAnimator.INFINITE
                loadingRec.startAnimation(animator0)

            if (radio_group.checkedRadioButtonId == -1){

                Toast.makeText(this, "Please Choose First Before Clicking Download", Toast.LENGTH_LONG).show()

                animator0.repeatCount = 1

                Timer().schedule(3000){
                    animator0.cancel()

                }
                Timer().schedule(3300){
                    loadingRec.startAnimation(animator1)
                }
                loadingRec.isClickable = true

            }
            if (radio_group.checkedRadioButtonId == R.id.glide_url){

                c = "Glide"
                download(GlideURL)

            }
            if (radio_group.checkedRadioButtonId == R.id.loadapp_url){

                c = "LoadApp"
                download(LoadAppURL)

            }
            if (radio_group.checkedRadioButtonId == R.id.retrofit_url){

                c = "RetroFit"
                download(RetroFitURL)

            }

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val cursor : Cursor = q.query(DownloadManager.Query().setFilterById(downloadID))

            if (cursor.moveToFirst()){

                val theStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                if (theStatus == DownloadManager.STATUS_SUCCESSFUL){
                  a = "Success"
                  theNotification(a, c)
                  animator0.cancel()
                    Timer().schedule(300){
                        loadingRec.startAnimation(animator1)
                    }
                  loading_rec.isClickable = true

                } else {
                  a ="Fail"
                  theNotification(a, c)
                  animator0.cancel()
                    Timer().schedule(300){
                        loadingRec.startAnimation(animator1)
                    }
                  loading_rec.isClickable = true

                }

            }

        }
    }

    private fun download(theUrl: String) {
        val request =
            DownloadManager.Request(Uri.parse(theUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS.toString(), getString(R.string.app_name) + ".zip" )

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val GlideURL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val LoadAppURL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
        private const val RetroFitURL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channelId"
    }


    private fun theNotification(b:String = "test", d:String){

        val notificationId = 0
        val theMainIntent = Intent(this, DetailActivity:: class.java)
        theMainIntent.putExtra("status", "$b")
        theMainIntent.putExtra("file name", "$d")
        val thePendingIntent = PendingIntent.getActivity(this, notificationId, theMainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val detailsIntent = Intent(this, DetailActivity::class.java)
        detailsIntent.putExtra("status", "$b")
        detailsIntent.putExtra("file name", "$d")
        val detailsPendingIntent = PendingIntent.getActivity(this, notificationId, detailsIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val theBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        theBuilder.setSmallIcon(R.drawable.ic_assistant_black_24dp)
        theBuilder.setContentTitle(this.getString(R.string.notification_title))
        theBuilder.setContentText(this.getString(R.string.notification_description))
        theBuilder.priority = NotificationCompat.PRIORITY_HIGH
        theBuilder.setContentIntent(thePendingIntent)
        theBuilder.addAction(R.drawable.ic_assistant_black_24dp, "Go To Details", detailsPendingIntent)
        theBuilder.setAutoCancel(true)
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, theBuilder.build())

    }

    private fun createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = "C1"

            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }


}
