package com.example.users.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

//import com.example.tasks.dtos.requests.AddRequestDto
//import io.ktor.http.*
//import io.ktor.server.application.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*

fun Application.configureUsersRoutes() {
    routing {
        route("/api/v1") {
            post("/tasks/store") {
            //val request = call.receive<AddRequestDto>()
            //
            //if(request.username.isBlank()){
            //    call.respond(HttpStatusCode.BadRequest, "Username is required")
            //    return@post
            //}
            //
            //if(request.email.isBlank()){
            //    call.respond(HttpStatusCode.BadRequest, "Email is required")
            //    return@post
            //}
            //if(request.email.isNotBlank()){
            //    val emailRegex = Regex("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$")
            //    if(!emailRegex.matches(request.email)){
            //        call.respond(HttpStatusCode.BadRequest, "Invalid email")
            //    }
            //
            //    if(){
            //
            //    }
            //}
            //
            //if(request.password.isBlank())
            //
            //call.respond(HttpStatusCode.OK, request)
            }
        }
    }
}
