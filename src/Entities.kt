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
}

interface PartnerService {
    fun createPartner(parceiro: Parceiro)
    fun getPartner(id: Int): Parceiro
    fun deletePartner(parceiro: Parceiro)
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
        var id = getInsertId()

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

}

class PartnerServiceImpl : PartnerService {

    val partners = HashMap<Int, Parceiro>()

    override fun createPartner(parceiro: Parceiro) {
        partners[parceiro.id] = parceiro
    }

    override fun getPartner(id: Int): Parceiro {
        if (!partners.containsKey(id)) {
            fail("Partners does not exist")
        }

        return partners[id]!!
    }

    override fun deletePartner(parceiro: Parceiro) {
        partners.remove(parceiro.id)
    }

}