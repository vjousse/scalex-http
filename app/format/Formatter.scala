package scalex.http
package format

import scalex.model.{ Def, Block }
import com.github.ornicar.paginator.PaginatorLike

object Formatter {

  def apply(
    query: String,
    paginator: PaginatorLike[_],
    defs: List[Def]): JsonObject = JsonObject(
    "query" -> query,
    "nbResults" -> paginator.nbResults,
    "page" -> paginator.currentPage,
    "nbPages" -> paginator.nbPages,
    "results" -> (defs map { fun ⇒
      JsonObject(
        "name" -> fun.name,
        "qualifiedName" -> fun.qualifiedName,
        "typeParams" -> sugar(fun.showTypeParams),
        "resultType" -> sugar(fun.resultType),
        "valueParams" -> sugar(fun.paramSignature),
        "signature" -> sugar(fun.signature),
        "package" -> fun.pack,
        "deprecation" -> (fun.deprecation map block),
        "parent" -> JsonObject(
          "name" -> fun.parent.name,
          "qualifiedName" -> fun.parent.qualifiedName,
          "typeParams" -> sugar(fun.parent.showTypeParams)
        ),
        "docUrl" -> fun.encodedDocUrl,
        "comment" -> (fun.comment map { com ⇒
          JsonObject(
            "short" -> block(com.short),
            "body" -> block(com.body),
            "authors" -> (com.authors map block),
            "see" -> (com.see map block),
            "result" -> optionBlock(com.result),
            "throws" -> JsonObject(com.throws map fixKey),
            "typeParams" -> JsonObject(com.typeParams map fixKey),
            "valueParams" -> JsonObject(com.valueParams map fixKey),
            "version" -> optionBlock(com.version),
            "since" -> optionBlock(com.since),
            "todo" -> (com.todo map block),
            "note" -> (com.note map block),
            "example" -> (com.example map block),
            "constructor" -> optionBlock(com.constructor),
            "source" -> com.source
          )
        })
      ) removeNones
    })
  )

  private val fixKey: PartialFunction[(String, Block), (String, JsonObject)] = {
    case (k, v) ⇒ (k.replace("_", ".") -> block(v))
  }

  private def optionBlock(b: Option[Block]): JsonObject =
    JsonObject("html" -> (b map (_.html)), "txt" -> (b map (_.txt)))

  private def block(b: Block): JsonObject =
    JsonObject("html" -> b.html, "txt" -> b.txt)

  def sugar(str: Any) = str.toString.replace("=>", "⇒")
}
