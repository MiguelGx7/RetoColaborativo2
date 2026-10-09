package model;

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // POST: enviamos las credenciales en el cuerpo (@Body)
    // "suspend" = se ejecuta dentro de una corrutina, sin congelar la app
    // Response<...> = nos deja revisar si salió bien (isSuccessful) o el código de error
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // GET protegido: el token viaja en la cabecera (@Header)
    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>
}
