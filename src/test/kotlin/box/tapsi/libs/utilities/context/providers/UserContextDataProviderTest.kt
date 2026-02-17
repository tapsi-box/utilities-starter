package box.tapsi.libs.utilities.context.providers

import box.tapsi.libs.utilities.context.ContextProperties
import box.tapsi.libs.utilities.localization.Language
import box.tapsi.libs.utilities.user.User
import io.grpc.Metadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class UserContextDataProviderTest {
  private val properties = ContextProperties()

  private val userContextDataProvider = UserContextDataProvider(properties)

  @Test
  fun `should return user when headers contain all necessary information`() {
    // given
    val headers = mock<Metadata>()
    // when
    whenever(headers.get(userContextDataProvider.requestHeaderUserIdKey)).thenReturn("123")
    whenever(headers.get(userContextDataProvider.requestHeaderUserRoleKey)).thenReturn("ADMIN")
    whenever(headers.get(userContextDataProvider.requestHeaderUserLanguageKey)).thenReturn("fa-IR")
    val result = userContextDataProvider.getDataFromHeaders(headers)
    // verify
    assertEquals("123", result?.id)
    assertEquals(User.Role.Admin, result?.role)
    assertEquals(Language.Persian, result?.language)
  }

  @Test
  fun `should return null when headers do not contain user id and role`() {
    // given
    val headers = mock<Metadata>()
    // when
    whenever(headers.get(userContextDataProvider.requestHeaderUserIdKey)).thenReturn(null)
    whenever(headers.get(userContextDataProvider.requestHeaderUserRoleKey)).thenReturn(null)
    val result = userContextDataProvider.getDataFromHeaders(headers)
    // verify
    assertNull(result)
  }

  @Test
  fun `should return user with default language when headers do not contain language`() {
    // given
    val headers = mock<Metadata>()
    // when
    whenever(headers.get(userContextDataProvider.requestHeaderUserIdKey)).thenReturn("123")
    whenever(headers.get(userContextDataProvider.requestHeaderUserRoleKey)).thenReturn("ADMIN")
    whenever(headers.get(userContextDataProvider.requestHeaderUserLanguageKey)).thenReturn(null)
    val result = userContextDataProvider.getDataFromHeaders(headers)
    // verify
    assertEquals(Language.default, result?.language)
  }

  @Test
  fun `should return user with ip address when headers contain ip address`() {
    // given
    val headers = mock<Metadata>()
    // when
    whenever(headers.get(userContextDataProvider.requestHeaderUserIdKey)).thenReturn("123")
    whenever(headers.get(userContextDataProvider.requestHeaderUserRoleKey)).thenReturn("ADMIN")
    whenever(headers.get(userContextDataProvider.requestHeaderUserLanguageKey)).thenReturn("fa-IR")
    whenever(headers.get(userContextDataProvider.requestHeaderUserIpKey)).thenReturn("127.0.0.1,32.86.10.11")
    val result = userContextDataProvider.getDataFromHeaders(headers)

    // verify
    assertEquals("127.0.0.1", result?.ip)
  }
}
