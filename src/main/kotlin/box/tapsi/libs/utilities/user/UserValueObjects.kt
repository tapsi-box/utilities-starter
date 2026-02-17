package box.tapsi.libs.utilities.user

import box.tapsi.libs.utilities.grpc.XAgentHeaderInfo
import box.tapsi.libs.utilities.localization.Language

@Suppress("detekt.LongParameterList")
open class User(
  val id: String,
  val role: Role,
  val language: Language,
  val platform: Platform?,
  val product: Product?,
  val appVersion: String?,
  val ip: String?,
  val globalId: String,
) {
  open var xAgent: XAgentHeaderInfo? = null

  constructor(
    id: String,
    role: Role,
    language: Language,
    xAgent: XAgentHeaderInfo?,
    ip: String?,
    globalId: String = "",
  ) : this(
    id = id,
    role = role,
    language = language,
    platform = xAgent?.platform?.let(Platform::findByName),
    appVersion = xAgent?.appVersion,
    product = xAgent?.product?.let(Product::findByName),
    ip = ip,
    globalId = globalId,
  ) {
    this.xAgent = xAgent
  }

  constructor(id: String, role: Role) : this(
    id = id,
    role = role,
    language = Language.default,
    platform = null,
    appVersion = null,
    product = null,
    ip = null,
    globalId = "",
  )

  enum class Role(val roleName: String?) {
    Biker("BIKER"),
    Driver("SCHEDULED_DELIVERY_DRIVER"),
    Taker("PASSENGER"),
    TapsiDriver("DRIVER"),
    Agent("AGENT"),
    Admin("ADMIN"),
    BackOffice("BACK-OFFICE"),
    DeliveryCourierApprover("DELIVERY_COURIER_APPROVER"),
    ChiefBackOffice("CHIEF-BACK-OFFICE"),
    CCSupervisor("CC_SUPERVISOR"),
    VOC("VOC"),
    DocVerificationAgent("DOC_VERIFICATION_AGENT"),
    DocVerificationVocAgent("DOC_VERIFICATION_VOC_AGENT"),
    CCFinanceManager("CC_FINANCE_MANAGER"),
    SecuritySupervisor("SECURITY_SUPERVISOR"),
    FraudAgent("FRAUD_AGENT"),
    SosAgentLevel1("SOS_AGENT_LEVEL_1"),
    SosAgentLevel2("SOS_AGENT_LEVEL_2"),
    TransferCreditApprover("TRANSFER_CREDIT_APPROVER"),
    TechLead("TECH_LEAD"),
    Unknown(roleName = null),
    Corporate("CORPORATE"),
    System("SYSTEM"),
    Server("SERVER"),
    ;

    companion object {
      fun findByRoleName(roleName: String): Role? = entries.find {
        it.roleName.equals(roleName, ignoreCase = true)
      }

      fun findByRoleNameOrThrow(roleName: String): Role = requireNotNull(findByRoleName(roleName)) {
        "Role name: $roleName is not valid"
      }
    }
  }

  enum class Platform(val platformName: String) {
    Android("ANDROID"),
    Ios("IOS"),
    Web("WEB"),
    AndroidWebview("ANDROID_WEBVIEW"),
    WebApp("WEBAPP"),
    External("EXTERNAL"),
    ;

    companion object {
      fun findByName(
        platformName: String,
      ): Platform? = entries.find { it.platformName.equals(platformName, ignoreCase = true) }
    }
  }

  enum class Product(val productName: String) {
    BackOffice("BACKOFFICEAPP"),
    ExternalEmbeddedApi("EXTERNAL_EMBEDDED_API"),
    CabPassenger("PASSENGER"),
    CabDriver("DRIVER"),
    TakerPanel("DELIVERY_TAKER_PANEL"),
    ;

    companion object {
      fun findByName(productName: String): Product? = entries
        .find {
          it.productName.equals(productName, ignoreCase = true)
        }
    }
  }

  companion object {
    const val UNKNOWN_USER_ID = "unknown"
    const val SYSTEM_USER_ID = "0"

    val unknownUser =
      User(id = UNKNOWN_USER_ID, language = Language.default, role = Role.Unknown, xAgent = null, ip = "")
    val systemUser = User(id = SYSTEM_USER_ID, role = Role.System)
  }
}
