data class Usuario(var id: Int, val nome: String, val email: String, val cpf: String)
data class Parceiro(var id: Int, val nome: String, val cnpj: String)
data class Ponto(var id: Int, val usuario: Usuario, val parceiro: Parceiro, val campanha: Campanha, val points: Int)
data class Campanha(var id: Int, val parceiro: Parceiro, val points: Int, val descricao: String)

interface CampaignService {
    fun createCampaign(campanha: Campanha)
    fun getCampaign(id: Int): Campanha
    fun deleteCampaign(campanha: Campanha)

    fun createPoints(user: Usuario, parceiro: Parceiro, campanha: Campanha)
}

interface UserService {
    suspend fun createUser(user: Usuario): Usuario
    suspend fun getUser(id: Int): Usuario
    suspend fun deleteUser(user: Usuario)
    suspend fun getAll(): Collection<Usuario>
}

interface PartnerService {
    suspend fun createPartner(parceiro: Parceiro): Parceiro
    suspend fun getPartner(id: Int): Parceiro
    suspend fun deletePartner(id: Int)
}

fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}

class CampaignServiceImpl : CampaignService {
    val campaign = HashMap<Int, Campanha>()
    val points = HashMap<Int, Ponto>()

    override fun createCampaign(campanha: Campanha) {
        campaign[campanha.id] = campanha
    }

    override fun getCampaign(id: Int): Campanha {
        if (!campaign.containsKey(id)) {
            fail("Campaign does not exists")
        }
        return campaign[id]!!
    }

    override fun deleteCampaign(campanha: Campanha) {
        campaign.remove(campanha.id)
    }

    override fun createPoints(user: Usuario, parceiro: Parceiro, campanha: Campanha) {
        //val id: Int, val usuario: Usuario, val parceiro: Parceiro, val campanha: Campanha, val points: Int)
        val id = getInsertId()

        val point = Ponto(id = id, usuario = user, parceiro = parceiro, campanha = campanha, points = campanha.points)

        points[id] = point
    }

    private fun getInsertId(): Int {

        val size = points.size

        if (size == 0) {
            return 0
        }

        val (id, _, _, _, _) = points[size - 1]!!

        return id + 1
    }
}

class UserServiceImpl : UserService {

    val users = HashMap<Int, Usuario>()

    override suspend fun createUser(user: Usuario): Usuario {
        users[user.id] = user
        return user
    }

    override suspend fun getUser(id: Int): Usuario {
        if (!users.containsKey(id)) {
            fail("User does not exists")
        }
        return users[id]!!
    }

    override suspend fun deleteUser(user: Usuario) {
        users.remove(user.id)
    }

    override suspend fun getAll(): Collection<Usuario> {
        val result = ArrayList<Usuario>()

        if (users.size > 0) {
            result.addAll(users.values)
        }

        return result
    }

}

class PartnerServiceImpl : PartnerService {

    val partners = HashMap<Int, Parceiro>()

    override suspend fun createPartner(parceiro: Parceiro): Parceiro {
        partners[parceiro.id] = parceiro
        return parceiro
    }

    override suspend fun getPartner(id: Int): Parceiro {
        if (!partners.containsKey(id)) {
            fail("Partners does not exist")
        }

        return partners[id]!!
    }

    override suspend fun deletePartner(id: Int) {
        partners.remove(id)
    }

}