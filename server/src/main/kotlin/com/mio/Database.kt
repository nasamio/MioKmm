import com.mio.UserService
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

lateinit var userService: UserService

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/kmm",
        user = "root",
        driver = "com.mysql.cj.jdbc.Driver",
        password = "123456"
    )

    userService = UserService(database)
}
