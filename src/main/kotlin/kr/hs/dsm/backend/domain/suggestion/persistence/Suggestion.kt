package kr.hs.dsm.backend.domain.suggestion.persistence

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType


/*
# 건의사항 상세 조회

## Header

### `Authorization`

- **STRING**
- JWT 액세스 토큰
- `Bearer b2iidkkdiskejfjv.dsjseilsjdlfe.tokaaweolfskeioswldkeosl`

## Path Param

### `suggestion_id`

- **LONG**
- 건의사항 id
- `134`

# Response

## 200 Ok

### Body

```json
{
	"id" : 1,
	"title" : "건의합니다",
	"created_at" : "2023-07-27'T'12:12:12",
	"image_url": "https://asldkmsa",
	"type" : "TRAFFIC",
	"latitude" : "37.5026432", //위도
  "longitude" : "37.5026432", //경도
	"description" : "ㅁㄴ아ㅣㅡㅁ니ㅏ으키ㅏㅡㅋㅋㅊㅌ"
}
```
 */

@Entity
class Suggestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    val title: String,

    val description: String,

    val type: SuggestionType,

    val latitude: BigDecimal,

    val longitude: BigDecimal,

    val imageUrl: String?,

    val createdAt: LocalDateTime = LocalDateTime.now()
)