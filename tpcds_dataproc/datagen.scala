import com.databricks.spark.sql.perf.tpcds.TPCDSTables

val rootDir = "<ROOT_DIR>"
val dsdgenDir = "/opt/oss_util/tpcds_dataproc/tpcds-kit/tools"
val scaleFactor = "3000"
val format = "parquet"
val databaseName = "tpcds3000"
val sqlContext = spark.sqlContext

val tables = new TPCDSTables(sqlContext,
dsdgenDir = dsdgenDir, 
scaleFactor = scaleFactor,
useDoubleForDecimal = true, 
useStringForDate = true)

tables.genData(
location = rootDir,
format = format,
overwrite = true,
partitionTables = true, 
clusterByPartitionColumns = true, 
filterOutNullPartitionValues = true, 
tableFilter = "", 
numPartitions = 100)

sql(s"create database $databaseName") 

tables.createExternalTables(rootDir, 
format, 
databaseName, 
overwrite = true, 
discoverPartitions = true)

tables.analyzeTables(databaseName, analyzeColumns = true)
