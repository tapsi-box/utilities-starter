package box.tapsi.libs.utilities.security.hash.services

import box.tapsi.libs.utilities.security.hash.HashAlgorithm

interface HashService {
  fun hash(input: String, algorithm: HashAlgorithm = HashAlgorithm.SHA256): String
}
