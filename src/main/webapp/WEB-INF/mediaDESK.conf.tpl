###################################################################
#
# mediaDESK Configuration File
# Maintained by Franz Stumpner
#
###################################################################

# history:
# --------
# 2005-04-28 created

httpBase=http://localhost:8080

# instanceName
# ------------
# Setting the Name of the Image Database
instanceName=openMEDIADESK

# lic
# ------------
# Lizenz
lic=DTRJH9WmKTbl7M1cDfQZEiEQhkJKSSuyssuqRkpuuMgALJhbMxG/m2R7s08aaAKKViQQ4wDIMYvJKf7+SavB3CZPBtZbV4QZhFEZUfUNpjGVloU8SIIcHWlSU/QNG2hmbr0H555LqQZu53TZPpCajxRvPDc7ZpSD

# licMaxMb
# --------
# Licensed for Store-Volume (wich comes first, maxImages or MaxMb
licMaxMb=1600
maxImageSize=100000

# fileSystemImportPath
# --------------------
# The Path where new ftp/filesystem import files beeing searched
fileSystemImportPath=C:\\devdata\\mediadesk\\fsip

# imageStorePath
# --------------
# The Path where the images are beeing saved
imageStorePath=C:\\devdata\\mediadesk\\

##############################################
# D A T A B A S E - C O N F I G U R A T I O N
##############################################

# db.jdbcDriver
# -------------
# The Classname of the JDBC-Driver to use for connecting to the RDBMS (f.e. org.postgresql.Driver, com.mysql.jdbc.Driver)
# attention: this driver is not distributed with this version and need to be downloaded an put into your /WEB-INF/lib directory
db.jdbcDriver=com.mysql.jdbc.Driver

# db.connectionUrl
# ----------------
# The Java Connction Url to the database
# for mysql5
db.connectionUrl=jdbc:mysql://localhost:3306/mediadesk31?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true

# db.username
# -----------
# The Username for the database - Server
db.username=root

# db.password
# -----------
# The Password for the database - Server
db.password=mediadesk
