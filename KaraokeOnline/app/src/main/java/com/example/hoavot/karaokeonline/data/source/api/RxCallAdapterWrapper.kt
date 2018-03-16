package com.example.hoavot.karaokeonline.data.source.api

import okhttp3.ResponseBody
import retrofit2.*
import java.lang.reflect.Type

/**
 *
 * @author at-hoavo.
 */
class RxCallAdapterWrapper<R>(private val type: Type, private val retrofit: Retrofit, private val wrapped: CallAdapter<R, *>?) : BaseRxCallAdapterWrapper<R>(type, retrofit, wrapped) {

    override fun convertRetrofitExceptionToCustomException(throwable: Throwable, retrofit: Retrofit): Throwable {

        if (throwable is HttpException) {
            val converter: Converter<ResponseBody, ApiException> = retrofit.responseBodyConverter(ApiException::class.java, arrayOfNulls<Annotation>(0))
            val response: Response<*>? = throwable.response()
            when (throwable.response().code()) {
//                HttpsURLConnection.HTTP_UNAUTHORIZED -> RxBus.publish()
            //Todo: Handle for another status code
            }
            response?.errorBody()?.let {
                return converter.convert(it)
            }
        }

        return throwable
    }

    override fun createExceptionForSuccessResponse(response: Any?): Throwable? = super.createExceptionForSuccessResponse(response)
}
