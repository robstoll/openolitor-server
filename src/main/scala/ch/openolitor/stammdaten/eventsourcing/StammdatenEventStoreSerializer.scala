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
package ch.openolitor.stammdaten.eventsourcing

import stamina._
import stamina.json._
import spray.json.lenses.JsonLenses._
import ch.openolitor.stammdaten._
import ch.openolitor.stammdaten.models._
import ch.openolitor.core.domain.EntityStore._
import ch.openolitor.core.domain.EntityStoreJsonProtocol
import ch.openolitor.stammdaten.models.LieferungPlanungAdd
import ch.openolitor.stammdaten.models.LieferungPlanungRemove
import ch.openolitor.stammdaten.StammdatenCommandHandler._
import ch.openolitor.core.eventsourcing.CoreEventStoreSerializer
import java.util.Locale

trait StammdatenEventStoreSerializer extends StammdatenJsonProtocol with EntityStoreJsonProtocol with CoreEventStoreSerializer {
  //V1 persisters
  implicit val depotModifyPersister = persister[DepotModify]("depot-modify")
  implicit val depotIdPersister = persister[DepotId]("depot-id")

  implicit val aboIdPersister = persister[AboId]("abo-id")

  implicit val abotypModifyPersister = persister[AbotypModify]("abotyp-modify")
  implicit val abotypIdPersister = persister[AbotypId]("abotyp-id")

  implicit val kundeModifyPersister = persister[KundeModify]("kunde-modify")
  implicit val kundeIdPersister = persister[KundeId]("kunde-id")

  implicit val personCreatePersister = persister[PersonCreate]("person-create")

  implicit val abwesenheitCreatePersister = persister[AbwesenheitCreate]("abwesenheit-create")
  implicit val abwesenheitIdPersister = persister[AbwesenheitId]("abwesenheit-id")

  implicit val vertriebModifyPersister = persister[VertriebModify]("vertrieb-modify")
  implicit val vertriebIdPersister = persister[VertriebId]("vertrieb-id")

  implicit val vertriebsartDLAbotypPersister = persister[DepotlieferungAbotypModify]("depotlieferungabotyp-modify")
  implicit val vertriebsartPLAbotypPersister = persister[PostlieferungAbotypModify]("postlieferungabotyp-modify")
  implicit val vertriebsartHLAbotypPersister = persister[HeimlieferungAbotypModify]("heimlieferungabotyp-modify")
  implicit val vertriebsartIdPersister = persister[VertriebsartId]("vertriebsart-id")

  implicit val vertriebsartDLPersister = persister[DepotlieferungModify]("depotlieferung-modify")
  implicit val vertriebsartPLPersister = persister[PostlieferungModify]("postlieferung-modify")
  implicit val vertriebsartHLPersister = persister[HeimlieferungModify]("heimlieferung-modify")

  implicit val aboGuthabenModifyPersister = persister[AboGuthabenModify]("abo-guthaben-modify")
  implicit val aboVertriebsartModifyPersister = persister[AboVertriebsartModify]("abo-vertriebsart-modify")
  implicit val aboDLPersister = persister[DepotlieferungAboModify]("depotlieferungabo-modify")
  implicit val aboPLPersister = persister[PostlieferungAboModify]("postlieferungabo-modify")
  implicit val aboHLPersister = persister[HeimlieferungAboModify]("heimlieferungabo-modify")

  implicit val customKundetypCreatePersister = persister[CustomKundentypCreate]("custom-kundetyp-create")
  implicit val customKundetypModifyPersister = persister[CustomKundentypModify]("custom-kundetyp-modify")
  implicit val customKundetypIdPersister = persister[CustomKundentypId]("custom-kundetyp-id")

  implicit val pendenzModifyPersister = persister[PendenzModify]("pendenz-modify")
  implicit val pendenzIdPersister = persister[PendenzId]("pendenz-id")
  implicit val pendenzCreatePersister = persister[PendenzCreate]("pendenz-create")

  implicit val lieferungAbotypCreatePersister = persister[LieferungAbotypCreate]("lieferung-abotyp-create")
  implicit val lieferungenAbotypCreatePersister = persister[LieferungenAbotypCreate]("lieferungen-abotyp-create")
  implicit val lieferungIdPersister = persister[LieferungId]("lieferung-id")
  implicit val lieferungModifyPersister = persister[LieferungModify]("lieferung-modify")
  implicit val lieferungPlanungAddPersister = persister[LieferungPlanungAdd]("lieferung-planungadd-modify")
  implicit val lieferungPlanungRemovePersister = persister[LieferungPlanungRemove]("lieferung-planungremove-modify")
  implicit val lieferplanungModifyPersister = persister[LieferplanungModify]("lieferplanung-modify")
  implicit val lieferplanungCreatePersister = persister[LieferplanungCreate]("lieferplanung-create")
  implicit val lieferplanungIdPersister = persister[LieferplanungId]("lieferplanung-id")
  implicit val lieferpositionModifyPersister = persister[LieferpositionModify]("lieferposition-modify")
  implicit val lieferpositionenCreatePersister = persister[LieferpositionenModify]("lieferpositionen-create")
  implicit val lieferpositionIdPersister = persister[LieferpositionId]("lieferposition-id")
  implicit val bestellungenCreatePersister = persister[BestellungenCreate]("bestellungen-create")
  implicit val bestellungCreatePersister = persister[BestellungCreate]("bestellung-create")
  implicit val bestellungModifyPersister = persister[BestellungModify]("bestellung-modify")
  implicit val bestellungIdPersister = persister[BestellungId]("bestellung-id")
  implicit val bestellpositionModifyPersister = persister[BestellpositionModify]("bestellposition-modify")
  implicit val bestellpositionIdPersister = persister[BestellpositionId]("bestellposition-id")
  implicit val auslieferungIdPersister = persister[AuslieferungId]("auslieferung-id")

  implicit val produktModifyPersister = persister[ProduktModify]("produkt-modify")
  implicit val produktIdPersister = persister[ProduktId]("produkt-id")

  implicit val produktkategorieModifyPersister = persister[ProduktekategorieModify]("produktekategorie-modify")
  implicit val produktkategorieIdPersister = persister[ProduktekategorieId]("produktekategorie-id")

  implicit val produzentModifyPersister = persister[ProduzentModify]("produzent-modify")
  implicit val produzentIdPersister = persister[ProduzentId]("produzent-id")

  implicit val tourCreatePersiter = persister[TourCreate]("tour-create")
  implicit val tourModifyPersiter = persister[TourModify]("tour-modify")
  implicit val tourIdPersister = persister[TourId]("tour-id")
  
  implicit val vorlageCreatePersister = persister[VorlageCreate]("vorlage-create")
  implicit val vorlageModifyPersister = persister[VorlageModify]("vorlage-modify")
  implicit val vorlageIdPersister = persister[VorlageId]("vorlage-id")

  val projektModifyPersister = persister[ProjektModify]("projekt-modify")
  implicit val projektModifyV2Persister = persister[ProjektModify, V2]("projekt-modify", from[V1]
    .to[V2](_.update('sprache ! set[Locale](Locale.GERMAN))))
  implicit val projektIdPersister = persister[ProjektId]("projekt-id")

  implicit val lieferplanungAbschliessenEventPersister = persister[LieferplanungAbschliessenEvent]("lieferplanung-abschliessen-event")
  implicit val lieferplanungAbrechnenEventPersister = persister[LieferplanungAbrechnenEvent]("lieferplanung-abrechnen-event")
  implicit val bestellungVersendenEventPersister = persister[BestellungVersendenEvent]("lieferung-bestellen-event")
  implicit val passwortGewechseltEventPersister = persister[PasswortGewechseltEvent]("passwort-gewechselt")

  implicit val korbCreatePersister = persister[KorbCreate]("korb-create")
  implicit val tourAuslieferungModifyPersister = persister[TourAuslieferungModify]("tour-auslieferung-modify")

  implicit val auslieferungAlsAusgeliefertMarkierenEventPersister = persister[AuslieferungAlsAusgeliefertMarkierenEvent]("auslieferung-als-ausgeliefert-markieren-event")
  implicit val setDefaultVorlageEventPersister = persister[SetDefaultVorlageEvent]("set-default-vorlage")

  val stammdatenPersisters = List(
    depotModifyPersister,
    depotIdPersister,
    aboIdPersister,
    abotypModifyPersister,
    abotypIdPersister,
    kundeModifyPersister,
    kundeIdPersister,
    personCreatePersister,
    pendenzIdPersister,
    pendenzCreatePersister,
    vertriebModifyPersister,
    vertriebIdPersister,
    vertriebsartDLPersister,
    vertriebsartPLPersister,
    vertriebsartHLPersister,
    vertriebsartIdPersister,
    vertriebsartDLAbotypPersister,
    vertriebsartPLAbotypPersister,
    vertriebsartHLAbotypPersister,
    aboDLPersister,
    aboPLPersister,
    aboHLPersister,
    aboGuthabenModifyPersister,
    aboVertriebsartModifyPersister,
    customKundetypCreatePersister,
    customKundetypModifyPersister,
    customKundetypIdPersister,
    pendenzModifyPersister,
    lieferungAbotypCreatePersister,
    lieferungenAbotypCreatePersister,
    lieferungIdPersister,
    lieferungModifyPersister,
    lieferungPlanungAddPersister,
    lieferungPlanungRemovePersister,
    lieferplanungModifyPersister,
    lieferplanungIdPersister,
    lieferplanungCreatePersister,
    lieferpositionModifyPersister,
    lieferpositionenCreatePersister,
    lieferpositionIdPersister,
    bestellungenCreatePersister,
    bestellungCreatePersister,
    bestellungModifyPersister,
    bestellungIdPersister,
    bestellpositionModifyPersister,
    bestellpositionIdPersister,
    produktIdPersister,
    produktModifyPersister,
    produktkategorieModifyPersister,
    produktkategorieIdPersister,
    produzentModifyPersister,
    produzentIdPersister,
    tourCreatePersiter,
    tourModifyPersiter,
    tourIdPersister,
    projektModifyV2Persister,
    projektIdPersister,
    abwesenheitCreatePersister,
    abwesenheitIdPersister,
    korbCreatePersister,
    tourAuslieferungModifyPersister,
    auslieferungIdPersister,
    vorlageCreatePersister,
    vorlageModifyPersister,
    vorlageIdPersister,

    //event persisters
    lieferplanungAbschliessenEventPersister,
    lieferplanungAbrechnenEventPersister,
    bestellungVersendenEventPersister,
    passwortGewechseltEventPersister,
    auslieferungAlsAusgeliefertMarkierenEventPersister,
    setDefaultVorlageEventPersister
  )
}