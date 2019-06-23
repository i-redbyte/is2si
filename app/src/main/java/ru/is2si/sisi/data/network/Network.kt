package ru.is2si.sisi.data.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import retrofit2.HttpException
import ru.is2si.sisi.R
import java.lang.reflect.Type

class Network(context: Context, private val gson: Gson) {
    private val unknowErrorText = context.getString(R.string.network_request_error)
    private val badRequestErrorText = context.getString(R.string.bad_request_error)
    private val unknownNetworkException =
        UnknownNetworkException(unknowErrorText)
    private val errorArrayType: Type = object : TypeToken<Error>() {}.type

    fun <T> prepareRequest(request: Single<T>): Single<T> = request.compose(errorValidation<T>())

    private fun <T> errorValidation(): SingleTransformer<T, T> = SingleTransformer { single ->
        single.onErrorResumeNext { Single.error(wrapError(it)) }
    }

    fun prepareRequest(request: Completable): Completable = request.compose(errorValidation())

    private fun errorValidation(): CompletableTransformer = CompletableTransformer { completable ->
        completable.onErrorResumeNext { Completable.error(wrapError(it)) }
    }

    private fun wrapError(throwable: Throwable): RuntimeException {
        if (throwable is HttpException) {

            val reader = throwable.response().errorBody()?.charStream() ?: return unknownNetworkException

            val error = try {
                gson.fromJson<Error>(reader, errorArrayType)
            } catch (e: JsonSyntaxException) {
                Error(unknowErrorText)
            } ?: return unknownNetworkException

            val message = when(throwable.code()) {
                400 -> badRequestErrorText
                else -> error.message ?: unknowErrorText
            }
            return NetworkException(message)
        } else {
            return unknownNetworkException
        }
    }

}

