package com.kieronquinn.app.smartspacer.repositories

import com.kieronquinn.app.smartspacer.BuildConfig
import com.kieronquinn.app.smartspacer.model.database.Grant
import com.kieronquinn.app.smartspacer.utils.extensions.firstNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

const val GRAPHITE_LAUNCHER_PACKAGE = "app.lawnchair"

interface GrantRepository {

    val grants: StateFlow<List<Grant>?>

    suspend fun getGrantForPackage(packageName: String): Grant?
    suspend fun addGrant(grant: Grant)

}

class GrantRepositoryImpl(
    private val databaseRepository: DatabaseRepository,
    scope: CoroutineScope = MainScope()
): GrantRepository {

    override val grants = databaseRepository.getGrants()
        .stateIn(scope, SharingStarted.Eagerly, null)

    override suspend fun getGrantForPackage(packageName: String): Grant? {
        if(packageName == BuildConfig.APPLICATION_ID){
            //Smartspacer always has full access
            return Grant(
                packageName,
                widget = true,
                smartspace = true,
                oemSmartspace = true,
                notifications = true
            )
        }
        if(packageName == GRAPHITE_LAUNCHER_PACKAGE){
            //Graphite: the ROM's launcher shows At a Glance without asking
            return Grant(packageName, smartspace = true)
        }
        return grants.firstNotNull().firstOrNull { it.packageName == packageName }
    }

    override suspend fun addGrant(grant: Grant) {
        databaseRepository.addGrant(grant)
    }

}