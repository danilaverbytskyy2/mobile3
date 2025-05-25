package com.example.app3fragment.server

import com.example.app3fragment.database.artist.Artist
import com.example.app3fragment.database.artist.ArtistDAO
import com.example.app3fragment.database.artist.ArtistRenameRequest
import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.database.fan.FanDAO
import com.example.app3fragment.database.fan.FanUpdateRequest
import com.example.app3fragment.database.label.Label
import com.example.app3fragment.database.label.LabelDAO
import com.example.app3fragment.database.label.LebelRenameRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Server {
    object KtorServerManager {
        private var server: EmbeddedServer<*, *>? = null

        fun start(scope: CoroutineScope, labelDao: LabelDAO, artistDao: ArtistDAO, fanDao: FanDAO) {
            if (server != null) return

            scope.launch {
                server = embeddedServer(CIO, 8080) {
                    install(ContentNegotiation) {
                        jackson {
                            enable(SerializationFeature.INDENT_OUTPUT)
                            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        }
                    }

                    routing {
                        get("/labels") {
                            call.respond(labelDao.getAll(), typeInfo<List<Label>>())
                        }

                        post("/labels/add") {
                            val label = call.receive<Label>()
                            labelDao.insert(label)
                            call.respond(HttpStatusCode.Created)
                        }

                        post("/labels/rem") {
                            val label = call.receive<Label>()
                            labelDao.delete(label)
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/labels/ren") {
                            val renameReq = call.receive<LebelRenameRequest>()
                            labelDao.updateName(renameReq.id, renameReq.newName)
                            call.respond(HttpStatusCode.OK)
                        }


                        get("/artists/by-label/{labelId}") {
                            val labelId = call.parameters["labelId"]?.toIntOrNull() ?: throw BadRequestException("Invalid label ID")
                            call.respond(artistDao.getArtistsByLabel(labelId))
                        }

                        post("/artists/add") {
                            val artist = call.receive<Artist>()
                            artistDao.insert(artist)
                            call.respond(HttpStatusCode.Created)
                        }

                        post("/artists/rem") {
                            val artist = call.receive<Artist>()
                            artistDao.delete(artist)
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/artists/ren") {
                            val renameReq = call.receive<ArtistRenameRequest>()
                            artistDao.updateName(renameReq.id, renameReq.newName)
                            call.respond(HttpStatusCode.OK)
                        }


                        get("/fans/by-fan/{fanId}") {
                            val programId = call.parameters["fanId"]?.toIntOrNull() ?: throw BadRequestException("Invalid fan ID")
                            val program = fanDao.getFanById(programId) ?: throw NotFoundException("Program not found")
                            call.respond(program)
                        }

                        get("/fans/by-artist/{artistId}") {
                            val artistId = call.parameters["artistId"]?.toIntOrNull() ?: throw BadRequestException("Invalid artist ID")
                            call.respond(fanDao.getFansByArtist(artistId))
                        }

                        post("/fans/add") {
                            val fan = call.receive<Fan>()
                            fanDao.insert(fan)
                            call.respond(HttpStatusCode.Created)
                        }

                        post("/fans/rem") {
                            val fan = call.receive<Fan>()
                            fanDao.delete(fan)
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/fans/update") {
                            val request = call.receive<FanUpdateRequest>()
                            request.name?.let { fanDao.updateName(request.id, it) }
                            request.description?.let { fanDao.updateDescription(request.id, it) }
                            request.developerPhone?.let { fanDao.updateDeveloperPhone(request.id, it) }
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }.start(wait = false)
            }
        }

        fun stop() {
            server?.stop(gracePeriodMillis = 1000, timeoutMillis = 5000)
            server = null
        }
    }
}