/*                                                                           *\
*    ____                   ____  ___ __                                      *
*   / __ \____  ___  ____  / __ \/ (_) /_____  _____                          *
*  / / / / __ \/ _ \/ __ \/ / / / / / __/ __ \/ ___/   OpenOlitor             *
* / /_/ / /_/ /  __/ / / / /_/ / / / /_/ /_/ / /       contributed by tegonal *
* \____/ .___/\___/_/ /_/\____/_/_/\__/\____/_/        http://openolitor.ch   *
*     /_/                                                                     *
*                                                                             *
* This program is free software: you can redistribute it and/or modify it     *
* under the terms of the GNU General Public License as published by           *
* the Free Software Foundation, either version 3 of the License,              *
* or (at your option) any later version.                                      *
*                                                                             *
* This program is distributed in the hope that it will be useful, but         *
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY  *
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for *
* more details.                                                               *
*                                                                             *
* You should have received a copy of the GNU General Public License along     *
* with this program. If not, see http://www.gnu.org/licenses/                 *
*                                                                             *
\*                                                                           */
package ch.openolitor.stammdaten

import spray.json._
import ch.openolitor.core.models._
import java.util.UUID
import org.joda.time._
import org.joda.time.format._
import ch.openolitor.core.BaseJsonProtocol
import ch.openolitor.stammdaten.models._
import com.typesafe.scalalogging.LazyLogging

/**
 * JSON Format deklarationen für das Modul Stammdaten
 */
trait StammdatenJsonProtocol extends BaseJsonProtocol with LazyLogging {

  //enum formats
  implicit val wochentagFormat = enumFormat(x => Wochentag.apply(x).getOrElse(Montag))
  implicit val monatFormat = enumFormat(x => Monat.apply(x).getOrElse(Januar))
  implicit val rhythmusFormat = enumFormat(Rhythmus.apply)
  implicit val preiseinheitFormat = new JsonFormat[Preiseinheit] {
    def write(obj: Preiseinheit): JsValue =
      obj match {
        case ProLieferung => JsString("Lieferung")
        case ProMonat => JsString("Monat")
        case ProQuartal => JsString("Quartal")
        case ProJahr => JsString("Jahr")
        case ProAbo => JsString("Abo")
      }

    def read(json: JsValue): Preiseinheit =
      json match {
        case JsString("Lieferung") => ProLieferung
        case JsString("Quartal") => ProQuartal
        case JsString("Monat") => ProMonat
        case JsString("Jahr") => ProJahr
        case JsString("Abo") => ProAbo
        case pe => sys.error(s"Unknown Preiseinheit:$pe")
      }
  }

  implicit val waehrungFormat = enumFormat(Waehrung.apply)
  implicit val laufzeiteinheitFormat = enumFormat(Laufzeiteinheit.apply)
  implicit val lieferungStatusFormat = enumFormat(LieferungStatus.apply)
  implicit val pendenzStatusFormat = enumFormat(PendenzStatus.apply)
  implicit val liefereinheitFormat = enumFormat(Liefereinheit.apply)

  //id formats
  implicit val vertriebsartIdFormat = baseIdFormat(VertriebsartId)
  implicit val abotypIdFormat = baseIdFormat(AbotypId)
  implicit val depotIdFormat = baseIdFormat(DepotId)
  implicit val tourIdFormat = baseIdFormat(TourId)
  implicit val kundeIdFormat = baseIdFormat(KundeId)
  implicit val pendenzIdFormat = baseIdFormat(PendenzId)
  implicit val personIdFormat = baseIdFormat(PersonId)
  implicit val aboIdFormat = baseIdFormat(AboId)
  implicit val lieferungIdFormat = baseIdFormat(LieferungId)
  implicit val customKundentypIdFormat = baseIdFormat(CustomKundentypId.apply)
  implicit val kundentypIdFormat = new RootJsonFormat[KundentypId] {
    def write(obj: KundentypId): JsValue =
      JsString(obj.id)

    def read(json: JsValue): KundentypId =
      json match {
        case JsString(id) => KundentypId(id)
        case kt => sys.error(s"Unknown KundentypId:$kt")
      }
  }
  implicit val produktIdFormat = baseIdFormat(ProduktId.apply)
  implicit val produktekategorieIdFormat = baseIdFormat(ProduktekategorieId.apply)
  implicit val baseProduktekategorieIdFormat = new JsonFormat[BaseProduktekategorieId] {
    def write(obj: BaseProduktekategorieId): JsValue =
      JsString(obj.id)

    def read(json: JsValue): BaseProduktekategorieId =
      json match {
        case JsString(id) => BaseProduktekategorieId(id)
        case kt => sys.error(s"Unknown BaseProduktekategorieId:$kt")
      }
  }
  implicit val produzentIdFormat = baseIdFormat(ProduzentId.apply)
  implicit val baseProduzentIdFormat = new JsonFormat[BaseProduzentId] {
    def write(obj: BaseProduzentId): JsValue =
      JsString(obj.id)

    def read(json: JsValue): BaseProduzentId =
      json match {
        case JsString(id) => BaseProduzentId(id)
        case kt => sys.error(s"Unknown BaseProduzentId:$kt")
      }
  }
  implicit val projektIdFormat = baseIdFormat(ProjektId.apply)

  implicit val lieferzeitpunktFormat = new RootJsonFormat[Lieferzeitpunkt] {
    def write(obj: Lieferzeitpunkt): JsValue =
      obj match {
        case w: Wochentag => w.toJson
        case _ => JsObject()
      }

    def read(json: JsValue): Lieferzeitpunkt =
      json.convertTo[Wochentag]
  }
  
  implicit val liefersaisonFormat = new RootJsonFormat[Liefersaison] {
    def write(obj: Liefersaison): JsValue =
      obj match {
        case m: Monat => m.toJson
        case _ => JsObject()
      }

    def read(json: JsValue): Liefersaison =
      json.convertTo[Monat]
  }

  implicit val depotlieferungFormat = jsonFormat4(Depotlieferung)
  implicit val heimlieferungFormat = jsonFormat4(Heimlieferung)
  implicit val postlieferungFormat = jsonFormat3(Postlieferung)

  implicit val depotFormat = jsonFormat22(Depot)
  implicit val depotModifyFormat = jsonFormat21(DepotModify)
  implicit val depotSummaryFormat = jsonFormat2(DepotSummary)
  implicit val tourFormat = jsonFormat3(Tour)
  implicit val tourModifyFormat = jsonFormat2(TourModify)

  implicit val postlieferungDetailFormat = jsonFormat3(PostlieferungDetail)
  implicit val depotlieferungDetailFormat = jsonFormat5(DepotlieferungDetail)
  implicit val heimlieferungDetailFormat = jsonFormat5(HeimlieferungDetail)

  implicit val vertriebsartDetailFormat = new RootJsonFormat[VertriebsartDetail] {
    def write(obj: VertriebsartDetail): JsValue =
      JsObject((obj match {
        case p: PostlieferungDetail => p.toJson
        case hl: HeimlieferungDetail => hl.toJson
        case dl: DepotlieferungDetail => dl.toJson
      }).asJsObject.fields + ("typ" -> JsString(obj.productPrefix.replaceAll("Detail", ""))))

    def read(json: JsValue): VertriebsartDetail =
      json.asJsObject.getFields("typ") match {
        case Seq(JsString("Postlieferung")) => json.convertTo[PostlieferungDetail]
        case Seq(JsString("Heimlieferung")) => json.convertTo[HeimlieferungDetail]
        case Seq(JsString("Depotlieferung")) => json.convertTo[DepotlieferungDetail]
      }
  }

  implicit val postlieferungModifyFormat = jsonFormat1(PostlieferungModify)
  implicit val depotlieferungModifyFormat = jsonFormat2(DepotlieferungModify)
  implicit val heimlieferungModifyFormat = jsonFormat2(HeimlieferungModify)

  implicit val vertriebsartModifyFormat = new RootJsonFormat[VertriebsartModify] {
    def write(obj: VertriebsartModify): JsValue =
      JsString(obj.productPrefix)

    def read(json: JsValue): VertriebsartModify = {
      if (!json.asJsObject.getFields("depotId").isEmpty) {
        json.convertTo[HeimlieferungModify]
      } else if (!json.asJsObject.getFields("tourId").isEmpty) {
        json.convertTo[HeimlieferungModify]
      } else {
        json.convertTo[PostlieferungModify]
      } 
    }
  }
  
  implicit val postlieferungAbotypModifyFormat = jsonFormat2(PostlieferungAbotypModify)
  implicit val depotlieferungAbotypModifyFormat = jsonFormat3(DepotlieferungAbotypModify)
  implicit val heimlieferungAbotypModifyFormat = jsonFormat3(HeimlieferungAbotypModify)

  implicit val vertriebsartAbotypModifyFormat = new RootJsonFormat[VertriebsartAbotypModify] {
    def write(obj: VertriebsartAbotypModify): JsValue =
      JsString(obj.productPrefix)

    def read(json: JsValue): VertriebsartAbotypModify = {
      if (!json.asJsObject.getFields("depotId").isEmpty) {
        json.convertTo[DepotlieferungAbotypModify]
      } else if (!json.asJsObject.getFields("tourId").isEmpty) {
        json.convertTo[HeimlieferungAbotypModify]
      } else {
        json.convertTo[PostlieferungAbotypModify]
      } 
    }
  }

  // json formatter which adds calculated boolean field  
  def enhanceWithBooleanFlag[E <: AktivRange](flag: String)(defaultFormat: JsonFormat[E]): RootJsonFormat[E] = new RootJsonFormat[E] {
    def write(obj: E): JsValue = JsObject(defaultFormat.write(obj).asJsObject.fields + (flag -> JsBoolean(obj.aktiv)))

    def read(json: JsValue): E = defaultFormat.read(json)
  }

  implicit val abotypFormat = enhanceWithBooleanFlag("aktiv")(jsonFormat18(Abotyp))
  implicit val abotypUpdateFormat = jsonFormat14(AbotypModify)
  implicit val abotypSummaryFormat = jsonFormat2(AbotypSummary)

  implicit val systemKundentypFormat = new JsonFormat[SystemKundentyp] {
    def write(obj: SystemKundentyp): JsValue =
      JsString(obj.productPrefix)

    def read(json: JsValue): SystemKundentyp =
      json match {
        case JsString(kundentyp) => SystemKundentyp.parse(kundentyp).getOrElse(sys.error(s"Unknown System-Kundentyp:$kundentyp"))
        case pt => sys.error(s"Unknown personentyp:$pt")
      }
  }

  implicit val customKundentypFormat = jsonFormat4(CustomKundentyp)

  implicit val kundentypFormat = new JsonFormat[Kundentyp] {
    def write(obj: Kundentyp): JsValue =
      obj match {
        case s: SystemKundentyp =>
          systemKundentypFormat.write(s)
        case c: CustomKundentyp =>
          customKundentypFormat.write(c)
      }

    def read(json: JsValue): Kundentyp =
      json match {
        case system: JsString => systemKundentypFormat.read(json)
        case custom: JsObject => customKundentypFormat.read(json)
        case pt => sys.error(s"Unknown personentyp:$pt")
      }
  }

  implicit val depotaboFormat = jsonFormat9(DepotlieferungAbo)
  implicit val depotaboModifyFormat = jsonFormat7(DepotlieferungAboModify)
  implicit val heimlieferungAboFormat = jsonFormat9(HeimlieferungAbo)
  implicit val heimlieferungAboModifyFormat = jsonFormat7(HeimlieferungAboModify)
  implicit val postlieferungAboFormat = jsonFormat7(PostlieferungAbo)
  implicit val postlieferungAboModifyFormat = jsonFormat5(PostlieferungAboModify)

  implicit val aboFormat = new RootJsonFormat[Abo] {
    def write(obj: Abo): JsValue =
      obj match {
        case d: DepotlieferungAbo => d.toJson
        case h: HeimlieferungAbo => h.toJson
        case p: PostlieferungAbo => p.toJson
        case _ => JsObject()
      }

    def read(json: JsValue): Abo = {
      if (!json.asJsObject.getFields("depotId").isEmpty) {
        json.convertTo[DepotlieferungAbo]
      } else if (!json.asJsObject.getFields("tourId").isEmpty) {
        json.convertTo[HeimlieferungAbo]
      } else {
        json.convertTo[PostlieferungAbo]
      } 
    }
  }

  implicit val aboModifyFormat = new RootJsonFormat[AboModify] {
    def write(obj: AboModify): JsValue =
      JsString(obj.productPrefix)

    def read(json: JsValue): AboModify = {
      logger.debug("Got new AboModify" + json.compactPrint)
      if (!json.asJsObject.getFields("depotId").isEmpty) {
        json.convertTo[DepotlieferungAboModify]
      } else if (!json.asJsObject.getFields("tourId").isEmpty) {
        json.convertTo[HeimlieferungAboModify]
      } else {
        json.convertTo[PostlieferungAboModify]
      } 
    }
  }

  implicit val personFormat = jsonFormat10(Person)
  implicit val personModifyFormat = jsonFormat8(PersonModify)
  implicit val kundeFormat = jsonFormat17(Kunde)
  implicit val pendenzFormat = jsonFormat6(Pendenz)
  implicit val pendenzModifyFormat = jsonFormat4(PendenzModify)
  implicit val kundeDetailFormat = jsonFormat20(KundeDetail)
  implicit val kundeModifyFormat = jsonFormat15(KundeModify)
  implicit val kundeSummaryFormat = jsonFormat2(KundeSummary)
  implicit val kundentypCreateFormat = jsonFormat2(CustomKundentypCreate)
  implicit val kundentypModifyFormat = jsonFormat1(CustomKundentypModify)

  implicit val lieferungFormat = jsonFormat6(Lieferung)
  implicit val lieferungAbotypCreateFormat = jsonFormat3(LieferungAbotypCreate)
  implicit val lieferungModifyFormat = jsonFormat1(LieferungModify)
  
  implicit val produktekategorieFormat = jsonFormat2(Produktekategorie)
  implicit val pModifyFormat = jsonFormat1(ProduktekategorieModify)
  
  implicit val produzentFormat = jsonFormat19(Produzent)
  implicit val produzentModifyFormat = jsonFormat18(ProduzentModify)
  
  implicit val produktFormat = jsonFormat9(Produkt)
  implicit val produktModifyFormat = jsonFormat8(ProduktModify)
  
  implicit val projektFormat = jsonFormat10(Projekt)
  implicit val projektModifyFormat = jsonFormat9(ProjektModify)
}
