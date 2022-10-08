package com.example.paging3expo.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3expo.data.model.Data
import com.example.paging3expo.data.remote.api.APIService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserDataSource(
    private val apiService: APIService
) : PagingSource<Int, Data>() {

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 20
    }
    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val currentLoadingPageKey = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getAllCharacters(currentLoadingPageKey)
            val user =  response.body()?.results ?: emptyList()
            val prevKey =
                if (currentLoadingPageKey == DEFAULT_PAGE_INDEX) null else currentLoadingPageKey - 1
            LoadResult.Page(
                data = user,
                prevKey = prevKey,
                nextKey = if (response.body() == null) null else currentLoadingPageKey + 1
            )
        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}