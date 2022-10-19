package com.pahnal.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.pahnal.mystoryapp.data.mapper.toDomain
import com.pahnal.mystoryapp.data.mapper.toDto
import com.pahnal.mystoryapp.data.paginator.StoryRemoteMediator
import com.pahnal.mystoryapp.data.source.local.dao.StoryDao
import com.pahnal.mystoryapp.data.source.local.room.StoryDatabase
import com.pahnal.mystoryapp.data.source.remote.network.ApiService
import com.pahnal.mystoryapp.data.vo.Resource
import com.pahnal.mystoryapp.domain.model.*
import com.pahnal.mystoryapp.domain.repository.IAuthRepository
import com.pahnal.mystoryapp.domain.repository.IStoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
open class MyRepository(
    private val database: StoryDatabase,
    private val apiService: ApiService,
) : IAuthRepository, IStoryRepository {

    open fun storyDao(): StoryDao = database.storyDao()

    override suspend fun login(login: LoginRequest): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(login.toDto())
            val userModel = response.toDomain()
            emit(Resource.Success(userModel))
        } catch (e: HttpException) {
            e.printStackTrace()
            val jsonObj = JSONObject(e.response()?.errorBody()!!.charStream().readText())
            emit(Resource.Error(jsonObj.getString("message") ?: "Couldn\'t not reach server"))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Couldn\'t load data"))
        }
    }

    override suspend fun register(register: RegisterRequest): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.register(register.toDto())
            emit(Resource.Success(response.message ?: "Success"))
        } catch (e: HttpException) {
            e.printStackTrace()
            val jsonObj = JSONObject(e.response()?.errorBody()!!.charStream().readText())
            emit(Resource.Error(jsonObj.getString("message") ?: "Couldn\'t not reach server"))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Couldn\'t load data"))
        }
    }

    override fun getAllStoriesFromDatabase(): LiveData<List<Story>> {
        return storyDao().getAllStories()
    }

    override fun getStoryById(id: String): LiveData<Story> {
        return storyDao().getStoryById(id)
    }

    override fun getAllStoriesRemoteMediator(token: String?): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(database, apiService, token),
            pagingSourceFactory = {
                storyDao().getAllStory()
            },
        ).liveData
    }

    override suspend fun addStory(
        file: MultipartBody.Part,
        addStory: AddStory,
        token: String?,
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.addNewStory(
                file,
                addStory.description.toRequestBody("text/plain".toMediaType()),
                if (addStory.lat != null) addStory.lat.toString()
                    .toRequestBody("text/plain".toMediaType()) else null,
                if (addStory.lat != null) addStory.lon.toString()
                    .toRequestBody("text/plain".toMediaType()) else null,
                token = "Bearer ${token ?: ""}",
            )
            emit(Resource.Success(response.message ?: "Success"))
        } catch (e: HttpException) {
            e.printStackTrace()
            val jsonObj = JSONObject(e.response()?.errorBody()!!.charStream().readText())
            emit(Resource.Error(jsonObj.getString("message") ?: "Couldn\'t not reach server"))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Couldn\'t load data"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MyRepository? = null

        @JvmStatic
        fun getInstance(database: StoryDatabase, apiService: ApiService): MyRepository {
            INSTANCE ?: synchronized(this::class.java) {
                INSTANCE = MyRepository(database, apiService)
            }
            return INSTANCE as MyRepository
        }
    }
}