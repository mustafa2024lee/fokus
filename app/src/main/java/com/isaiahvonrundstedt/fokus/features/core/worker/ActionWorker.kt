package com.isaiahvonrundstedt.fokus.features.core.worker

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.WorkerParameters
import com.isaiahvonrundstedt.fokus.components.service.NotificationActionService
import com.isaiahvonrundstedt.fokus.database.repository.TaskRepository
import com.isaiahvonrundstedt.fokus.features.shared.abstracts.BaseWorker

// This worker's primary function perform the action
// at is triggered in the fokus such as 'Mark as Finished'
class ActionWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: TaskRepository
) : BaseWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val action = inputData.getString(NotificationActionService.EXTRA_ACTION)
        val taskID = inputData.getString(NotificationActionService.EXTRA_TASK_ID)
        if (action.isNullOrBlank() || taskID.isNullOrBlank())
            return Result.success()

        if (action == NotificationActionService.ACTION_FINISHED)
            repository.setFinished(taskID, true)

        return Result.success()
    }

}