package com.example.app3fragment.server

import com.example.app3fragment.database.company.Company
import com.example.app3fragment.database.company.CompanyDAO
import com.example.app3fragment.database.company.CompanyRenameRequest
import com.example.app3fragment.database.program.Program
import com.example.app3fragment.database.program.ProgramDAO
import com.example.app3fragment.database.program.ProgramUpdateRequest
import com.example.app3fragment.database.sector.Sector
import com.example.app3fragment.database.sector.SectorDAO
import com.example.app3fragment.database.sector.SectorRenameRequest
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

        fun start(scope: CoroutineScope, sectorDao: SectorDAO, companyDao: CompanyDAO, programDao: ProgramDAO) {
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
                        get("/sectors") {
                            call.respond(sectorDao.getAll(), typeInfo<List<Sector>>())
                        }

                        post("/sectors/add") {
                            val sector = call.receive<Sector>()
                            sectorDao.insert(sector)
                            call.respond(HttpStatusCode.Created)
                        }

                        post("/sectors/rem") {
                            val sector = call.receive<Sector>()
                            sectorDao.delete(sector)
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/sectors/ren") {
                            val renameReq = call.receive<SectorRenameRequest>()
                            sectorDao.updateName(renameReq.id, renameReq.newName)
                            call.respond(HttpStatusCode.OK)
                        }


                        get("/companies/by-sector/{sectorId}") {
                            val sectorId = call.parameters["sectorId"]?.toIntOrNull() ?: throw BadRequestException("Invalid sector ID")
                            call.respond(companyDao.getCompaniesBySector(sectorId))
                        }

                        post("/companies/add") {
                            val company = call.receive<Company>()
                            companyDao.insert(company)
                            call.respond(HttpStatusCode.Created)
                        }

                        post("/companies/rem") {
                            val company = call.receive<Company>()
                            companyDao.delete(company)
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/companies/ren") {
                            val renameReq = call.receive<CompanyRenameRequest>()
                            companyDao.updateName(renameReq.id, renameReq.newName)
                            call.respond(HttpStatusCode.OK)
                        }


                        get("/programs/by-prog/{programId}") {
                            val programId = call.parameters["programId"]?.toIntOrNull() ?: throw BadRequestException("Invalid program ID")
                            val program = programDao.getProgramById(programId) ?: throw NotFoundException("Program not found")
                            call.respond(program)
                        }

                        get("/programs/by-company/{companyId}") {
                            val companyId = call.parameters["companyId"]?.toIntOrNull() ?: throw BadRequestException("Invalid company ID")
                            call.respond(programDao.getProgramsByCompany(companyId))
                        }

                        post("/programs/add") {
                            val program = call.receive<Program>()
                            programDao.insert(program)
                            call.respond(HttpStatusCode.Created)
                        }

                        post("/programs/rem") {
                            val program = call.receive<Program>()
                            programDao.delete(program)
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/programs/update") {
                            val request = call.receive<ProgramUpdateRequest>()
                            request.name?.let { programDao.updateName(request.id, it) }
                            request.description?.let { programDao.updateDescription(request.id, it) }
                            request.developerPhone?.let { programDao.updateDeveloperPhone(request.id, it) }
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