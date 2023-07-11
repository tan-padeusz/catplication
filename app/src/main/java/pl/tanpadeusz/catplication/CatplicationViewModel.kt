package pl.tanpadeusz.catplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.tanpadeusz.catplication.data.ImageResponse
import pl.tanpadeusz.catplication.data.VoteRequest
import pl.tanpadeusz.catplication.data.VoteResponse
import pl.tanpadeusz.catplication.service.CatplicationService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class CatplicationViewModel: ViewModel() {
    private val _imageResponse: MutableLiveData<ImageResponse?> = MutableLiveData()
    val imageResponse: LiveData<ImageResponse?> = this._imageResponse

    private val _voteResponse: MutableLiveData<VoteResponse?> = MutableLiveData()
    val voteResponse: LiveData<VoteResponse?> = this._voteResponse

    fun getImage() {
        Timber.d("get image")
        viewModelScope.launch {
            val response = try {
                CatplicationService.instance.getImage()
            } catch (ex: HttpException) {
                Timber.e("unexpected http exception : ${ex.message}")
                null
            } catch (ex: IOException) {
                Timber.e("unexpected io exception : ${ex.message}")
                null
            } ?: return@launch
            Timber.d("is response successful : ${response.isSuccessful} | code : ${response.code()} | message : ${response.message()}")
            val body = response.body()
            this@CatplicationViewModel._imageResponse.value = if (body.isNullOrEmpty()) null else body[0]
        }
    }

    fun postYayVote() {
        Timber.d("post yay vote")
        this.postVote(1)
    }

    fun postNayVote() {
        Timber.d("post nay vote")
        this.postVote(-1)
    }

    fun postVote(value: Int) {
        Timber.d("post vote with value : $value")
        if (this._imageResponse.value == null)
        {
            Timber.e("cannot vote for null image")
            this._voteResponse.value = null
            return
        }
        val voteRequest = VoteRequest(this._imageResponse.value!!.id, value)
        viewModelScope.launch {
            val response = try {
                CatplicationService.instance.postVote(voteRequest)
            } catch (ex: HttpException) {
                Timber.e("unexpected http exception: ${ex.message}")
                null
            } catch (ex: IOException) {
                Timber.e("unexpected io exception: ${ex.message}")
                null
            } ?: return@launch
            Timber.d("is response successful : ${response.isSuccessful} | code : ${response.code()} | message : ${response.message()}")
            val body = response.body()
            this@CatplicationViewModel._voteResponse.value = body
        }
    }
}