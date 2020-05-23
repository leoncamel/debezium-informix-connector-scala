package laoflch.debezium.connector.informix

import org.apache.kafka.connect.data.Schema
import org.apache.kafka.connect.data.Struct
import io.debezium.config.CommonConnectorConfig
import io.debezium.connector.AbstractSourceInfoStructMaker
import laoflch.debezium.connector.informix.SourceInfo


class InformixSourceInfoStructMaker(val connector: String, val version: String, val connectorConfig: CommonConnectorConfig)
  extends AbstractSourceInfoStructMaker[SourceInfo](connector, version, connectorConfig) {


  val schema_entry = commonSchemaBuilder.name("laoflch.debezium.connector.informix.Source")
    .field(SourceInfo.SCHEMA_NAME_KEY, Schema.STRING_SCHEMA)
    .field(SourceInfo.TABLE_NAME_KEY, Schema.STRING_SCHEMA)
    .field(SourceInfo.CHANGE_LSN_KEY, Schema.OPTIONAL_STRING_SCHEMA)
    .field(SourceInfo.COMMIT_LSN_KEY, Schema.OPTIONAL_STRING_SCHEMA).build

  def schema(): Schema = {
    schema_entry
  }

  override def struct(sourceInfo: SourceInfo): Struct = {
    val ret = super.commonStruct(sourceInfo).put(SourceInfo.SCHEMA_NAME_KEY, sourceInfo.getTableId.schema).put(SourceInfo.TABLE_NAME_KEY, sourceInfo.getTableId.table)
    if (sourceInfo.getChangeLsn >0l) ret.put(SourceInfo.CHANGE_LSN_KEY, sourceInfo.getChangeLsn.toString)
    if (sourceInfo.getCommitLsn >0l) ret.put(SourceInfo.COMMIT_LSN_KEY, sourceInfo.getCommitLsn.toString)
    if (sourceInfo.getTxId >0l) ret.put(SourceInfo.TX_ID, sourceInfo.getTxId.toString)
    if (sourceInfo.getBeginLsn >0l) ret.put(SourceInfo.BEGIN_LSN_KEY, sourceInfo.getBeginLsn.toString)
    ret
  }
}

