import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

fun main(){
    BDOper().makeTable()
}
class BDOper {
    init {

        try {
            Database.connect(
                    {
                        DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "")
                    }
            )
            println("Подключение успешно выполнено")
        } catch (e: Exception) {
            println(e)
            println("не получилось")
        }
    }

    fun CheckUser(login:String,pass:String):Boolean{
       return transaction {
            return@transaction Acc.find { (Accs.login eq login) and (Accs.password eq pass) }.count()==1
        }
    }

    fun isUserFree(login: String):Boolean{

       return transaction {

            return@transaction Acc.find{Accs.login eq login}.count()==0
        }
    }

    fun addUser(login: String,pass: String){
        transaction {
            addLogger(StdOutSqlLogger)
            Acc.new {
                this.login = login
                this.password = pass
            }
        }
    }

    fun makeTable(){
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(Accs)
        }
    }

    class Acc(id: EntityID<Int>) : Entity<Int>(id) {
        companion object : EntityClass<Int, Acc>(Accs)

        var login by Accs.login
        var password by Accs.password
    }

    object Accs : IntIdTable("logspasses") {
        val login = varchar("login", 30).primaryKey()
        val password = varchar("password", 200)
    }
}