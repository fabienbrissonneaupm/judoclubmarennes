package biz.ei6.judo.framework

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ServerPollWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // TODO: appeler ton serveur ici (Retrofit/Ktor/etc.)
        // Exemple: val hasUpdate = serverClient.checkSomething()

        val hasUpdate = true // placeholder

        if (hasUpdate) {
            Notif.ensureChannel(applicationContext)
            val pi = openAppPendingIntent(applicationContext)

            Notif.show(
                applicationContext,
                title = "Nouvelle info disponible",
                text = "Appuie pour ouvrir l’application",
                pendingIntent = pi
            )
        }

        return Result.success()
    }
}
