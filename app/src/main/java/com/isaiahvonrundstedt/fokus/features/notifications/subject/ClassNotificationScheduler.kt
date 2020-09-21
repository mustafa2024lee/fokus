package com.isaiahvonrundstedt.fokus.features.notifications.subject

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import com.isaiahvonrundstedt.fokus.database.repository.SubjectRepository
import com.isaiahvonrundstedt.fokus.features.shared.abstracts.BaseWorker

class ClassNotificationScheduler(context: Context, workerParameters: WorkerParameters)
    : BaseWorker(context, workerParameters) {

    private val subjectRepository by lazy { SubjectRepository.getInstance(applicationContext) }

    override suspend fun doWork(): Result {
        val subjectList = subjectRepository.fetch()

        subjectList.forEach { resource ->
            resource.schedules.forEach {
                it.subject = resource.subject.code

                val request = OneTimeWorkRequest.Builder(ClassNotificationWorker::class.java)
                    .setInputData(convertScheduleToData(it))
                workManager.enqueueUniqueWork(it.scheduleID, ExistingWorkPolicy.REPLACE,
                    request.build())
            }
        }
        return Result.success()
    }
}