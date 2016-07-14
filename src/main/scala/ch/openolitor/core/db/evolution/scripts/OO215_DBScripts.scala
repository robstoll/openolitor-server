package ch.openolitor.core.db.evolution.scripts

import ch.openolitor.core.db.evolution.Script
import com.typesafe.scalalogging.LazyLogging
import ch.openolitor.stammdaten.StammdatenDBMappings
import scalikejdbc._
import ch.openolitor.core.SystemConfig
import scala.util.{ Try, Success }

object OO215_DBScripts {

  val StammdatenDBScript = new Script with LazyLogging with StammdatenDBMappings with DefaultDBScripts {
    def execute(sysConfig: SystemConfig)(implicit session: DBSession): Try[Boolean] = {
      alterTableAddColumnIfNotExists(tourMapping, "anzahl_abonnenten", "int not null", "beschreibung")

      Success(true)
    }
  }

  val scripts = Seq(StammdatenDBScript)
}