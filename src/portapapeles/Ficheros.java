package portapapeles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para manejar ficheros {@link List} de {@link File}
 * 
 * @author: Pavon
 * @version: 10/05/2020
 * @since 1.0
 */

public class Ficheros {

	public List<File> ficheros;

	/**
	 * Constructor para definir los ficheros a manejar en la clase.
	 * 
	 * @param rutas lista con las rutas a manejar.
	 */

	public Ficheros(List<?> rutas) {
		ficheros = new ArrayList<File>();
		for (Object object : rutas) {
			String string = object.toString();
			ficheros.add(new File(string));
		}

	}

	/**
	 * devuelve si es carpeta un fichero.
	 * 
	 * @param fichero objeto manejador de fichero.
	 * @return <code>true</code> si es carpeta, <code>false</code> si no es carpeta.
	 */

	public boolean esCarpeta(File fichero) {
		return fichero.isDirectory();
	}

	/**
	 * devuelve si es fichero.
	 * 
	 * @param fichero objeto manejador de fichero.
	 * @return <code>true</code> si es fichero, <code>false</code> si no es fichero.
	 */

	public boolean esFichero(File fichero) {
		return fichero.isFile();
	}

	/**
	 * devuelve extension de un fichero.
	 * 
	 * @param nombre {@link String} nombre del fichero a obtener extension.
	 * @return {@link String} extension de fichero.
	 */

	public static String getExtensionFichero(String nombre) {
		if (nombre.lastIndexOf(".") != -1 && nombre.lastIndexOf(".") != 0)
			return nombre.substring(nombre.lastIndexOf(".") + 1);
		else
			return "";
	}

	/**
	 * devuelve un {@link String} con la extension de un fichero.
	 * 
	 * @param ruta ruta de un fichero para obtener extension.
	 * @return {@link String} con el tipo de extension de un fichero.
	 */

	public String tipoFichero(String ruta) {
		String extension = null;
		if (!ruta.equals("carpeta")) {
			extension = getExtensionFichero(ruta).toUpperCase();
		} else {
			extension = ruta.toUpperCase();
		}

		String tipo = "";
		switch (extension) {
		case "CARPETA":
			tipo = "carpeta";
			break;
		case "ABW":
		case "ACL":
		case "AFP":
		case "AMI":
		case "ANS":
		case "ASC":
		case "AWW":
		case "CCF":
		case "CWK":
		case "DBK":
		case "DOC":
		case "DOCM":
		case "DOCX":
		case "DOT":
		case "DOTX":
		case "DWD":
		case "EGT":
		case "EPUB":
		case "EZW":
		case "FDX":
		case "FTM":
		case "FTX":
		case "GDOC":
		case "HWP":
		case "HWPML":
		case "LOG":
		case "LWP":
		case "MBP":
		case "MD":
		case "ME":
		case "MCW":
		case "MOBI":
		case "NB":
		case "NBP":
		case "NEIS":
		case "ODM":
		case "ODOC":
		case "OSHEET":
		case "OTT":
		case "OMM":
		case "PAGINAS":
		case "PAP":
		case "PDAX":
		case "QUOX":
		case "RTF":
		case "RPT":
		case "SDW":
		case "SE":
		case "STW":
		case "SXW":
		case "TEX":
		case "TEXINFO":
		case "TROFF":
		case "TXT":
		case "UOF":
		case "UOML":
		case "VIA":
		case "WPD":
		case "WPS":
		case "WPT":
		case "WRD":
		case "WRF":
		case "WRI":
		case "XHTML":
		case "XML":
			tipo = "fichero texto";
			break;
		case "ADB":
		case "ADS":
		case "AHK":
		case "APPLESCRIPT":
		case "AS":
		case "AU3":
		case "BAT":
		case "BAS":
		case "CLJS":
		case "CMD":
		case "COFFEE":
		case "C":
		case "CPP":
		case "INO":
		case "EGG":
		case "ERB":
		case "HTA":
		case "IBI":
		case "ICI":
		case "IJS":
		case "IPYNB":
		case "JS":
		case "ITCL":
		case "JSFL":
		case "LUA":
		case "M":
		case "MRC":
		case "NCF":
		case "NUC":
		case "NUD":
		case "NUT":
		case "PDE":
		case "PHP":
		case "PL":
		case "PM":
		case "PS1":
		case "PS1XML":
		case "PSC1":
		case "PSD1":
		case "PSM1":
		case "PY":
		case "PYC":
		case "PYO":
		case "R":
		case "RDB":
		case "RB":
		case "RED":
		case "RS":
		case "SB2":
		case "SCPT":
		case "SCPTD":
		case "SDL":
		case "SH":
		case "TCL":
		case "XPL":
		case "SYPY":
		case "SYJS":
		case "TNS":
		case "VBS":
		case "ADA":
		case "BB":
		case "BMX":
		case "ASM":
		case "S":
		case "CLJ":
		case "CLS":
		case "CC":
		case "CXX":
		case "CBP":
		case "CS":
		case "CSPROJ":
		case "D":
		case "DBA":
		case "DBPRO123":
		case "E":
		case "EFS":
		case "FTN":
		case "EL":
		case "FOR":
		case "F":
		case "F77":
		case "F90":
		case "FRM":
		case "FRX":
		case "FTH":
		case "GED":
		case "GM6":
		case "GMD":
		case "GMK":
		case "GML":
		case "GO":
		case "H":
		case "HPP":
		case "HS":
		case "I":
		case "INC":
		case "JAVA":
		case "L":
		case "LGT":
		case "LISP":
		case "M4":
		case "ML":
		case "MSQR":
		case "N":
		case "P":
		case "PAS":
		case "PP":
		case "PHP3":
		case "PHP4":
		case "PHP5":
		case "PHPS":
		case "PHTML":
		case "PIV":
		case "PLI":
		case "PL1":
		case "PRG":
		case "PRO":
		case "POL":
		case "REDS":
		case "RESX":
		case "RC":
		case "RC2":
		case "RKT":
		case "RKTL":
		case "SCALA":
		case "SCI":
		case "SCM":
		case "SD7":
		case "SKB":
		case "SKC":
		case "SKD":
		case "SKF":
		case "SKG":
		case "SKI":
		case "SKK":
		case "SKM":
		case "SKO":
		case "SKP":
		case "SKQ":
		case "SKS":
		case "SKT":
		case "SKZ":
		case "SWG":
		case "VB":
		case "VBG":
		case "VBP":
		case "VIP":
		case "VBPROJ":
		case "VCPROJ":
		case "VDPROJ":
		case "XQ":
		case "XSL":
		case "Y":
		case "ATOM":
		case "EML":
		case "JSONLD":
		case "MET":
		case "METALINK":
		case "RSS":
		case "MARKDOWN":
		case "HTML":
		case "DTD":
		case "ASP":
			tipo = "codigo";
			break;
		case "8BF":
		case "A":
		case "APK":
		case "APP":
		case "BAC":
		case "BL":
		case "BUNDLE":
		case "CLASS":
		case "COFF":
		case "COM":
		case "DCU":
		case "DLL":
		case "DOL":
		case "EAR":
		case "ELF":
		case "EXPANDER":
		case "EXE":
		case "IPA":
		case "JEFF":
		case "JAR":
		case "XPI":
		case "PKZIP":
		case "NLM":
		case "O":
		case "OBB":
		case "RLL":
		case "S1ES":
		case "SO":
		case "VAP":
		case "WAR":
		case "XBE":
		case "XAP":
		case "XCOFF":
		case "XEX":
		case "VBX":
		case "OCX":
		case "TBL":
			tipo = "ejecutable";
			break;
		case "FITS":
		case "SILO":
		case "SPC":
		case "EAS3":
		case "EOSSA":
		case "OST":
		case "CCP4":
		case "HITRAN":
		case "SDF":
		case "NETCDF":
		case "HDF":
		case "CDF":
		case "CGNS":
		case "FMF":
		case "GRIB":
		case "MOL":
		case "SD":
		case "DX":
		case "JDX":
		case "SMI":
		case "G6":
		case "S6":
		case "AB1":
		case "ACE":
		case "BAM":
		case "BCF":
		case "BED":
		case "CAF":
		case "CRAM":
		case "DDBJ":
		case "EMBL":
		case "GFF":
		case "PLN":
		case "SAM":
		case "DCM":
		case "NII":
		case "GII":
		case "MGH":
		case "MGZ":
		case "MNC":
			tipo = "datos cientificos";
			break;
		case "AAF":
		case "3GP":
		case "GIF":
		case "ASF":
		case "AVCHD":
		case "AVI":
		case "BIK":
		case "CAM":
		case "COLLAB":
		case "DAT":
		case "DSH":
		case "FLV":
		case "M1V":
		case "M2V":
		case "FLA":
		case "FLR":
		case "SOL":
		case "M4V":
		case "MKV":
		case "WRAP":
		case "MNG":
		case "MOV":
		case "MPEG":
		case "MPG":
		case "MPE":
		case "THP":
		case "MP4":
		case "MXF":
		case "ROQ":
		case "NSV":
		case "RM":
		case "SMK":
		case "SWF":
		case "WMV":
		case "WTV":
		case "YUV":
		case "WEBM":
			tipo = "video";
			break;
		case "VFD":
		case "VHD":
		case "VUD":
		case "VMC":
		case "VSV":
		case "VMDK":
		case "NVRAM":
		case "VMEM":
		case "VMSD":
		case "VMSN":
		case "VMSS":
		case "STD":
		case "VMTM":
		case "VMX":
		case "CFG":
		case "VMXF":
		case "VBOX-EXTPACK":
		case "VDI":
		case "HDD":
		case "PVS":
		case "SAV":
		case "COW":
		case "QCOW":
		case "QED":
			tipo = "maquina virtual";
			break;
		case "ISO":
		case "NRG":
		case "IMG":
		case "ADF":
		case "ADZ":
		case "DMS":
		case "DSK":
		case "D64":
		case "SDI":
		case "MDS":
		case "MDX":
		case "DMG":
		case "CDI":
		case "CUE":
		case "B6T":
			tipo = "disco";
			break;
		case "123":
		case "AB2":
		case "AB3":
		case "AWS":
		case "BCSV":
		case "CLF":
		case "CELL":
		case "CSV":
		case "CSHEET":
		case "LCW":
		case "ODT":
		case "ODS":
		case "QPW":
		case "SDC":
		case "SLK":
		case "STC":
		case "SXC":
		case "TAB":
		case "VC":
		case "WK1":
		case "WK3":
		case "WK4":
		case "WKS":
		case "WQ1":
		case "XLK":
		case "XLS":
		case "XLSB":
		case "XLSM":
		case "XLSX":
		case "XLR":
		case "XLT":
		case "XLTM":
		case "XLW":
		case "TSV":
		case "DIF":
			tipo = "hoja_calculo";
			break;
		case "8SVX":
		case "16SVX":
		case "AIFF":
		case "AIF":
		case "AIFC":
		case "AU":
		case "BWF":
		case "CDDA":
		case "RAW":
		case "WAV":
		case "RA":
		case "FLAC":
		case "LA":
		case "PAC":
		case "APE":
		case "OFR":
		case "OFS":
		case "OFF":
		case "RKA":
		case "RKAU":
		case "SHN":
		case "TAK":
		case "THD":
		case "TTA":
		case "WV":
		case "WMA":
		case "BRSTM":
		case "DTS":
		case "DTSHD":
		case "DTSMA":
		case "AST":
		case "AW":
		case "PSF":
		case "AC3":
		case "AMR":
		case "MP1":
		case "MP2":
		case "MP3":
		case "GSM":
		case "OGG":
		case "AAC":
		case "MPC":
		case "VQF":
		case "OTS":
		case "SWA":
		case "VOX":
		case "VOC":
		case "SMP":
		case "MOD":
		case "MT2":
		case "S3M":
		case "XM":
		case "IT":
		case "NSF":
		case "MID":
		case "MIDI":
		case "LY":
		case "MUS":
		case "MUSX":
		case "MXL":
		case "MSCX":
		case "MSCZ":
		case "SIB":
		case "NIFF":
		case "PTB":
		case "CUST":
		case "GYM":
		case "JAM":
		case "RMJ":
		case "SID":
		case "SCP":
		case "TXM":
		case "VGM":
		case "YM":
		case "PVD":
			tipo = "audio";
			break;
		case "DVI":
		case "PLD":
		case "PCL":
		case "PDF":
		case "PS":
		case "SNP":
		case "XPS":
		case "CSS":
		case "XSLT":
		case "TPL":
			tipo = "documento";
			break;
		case "GXK":
		case "SSH":
		case "PUB":
		case "PPK":
		case "CER":
		case "CRT":
		case "DER":
		case "P7B":
		case "P7C":
		case "P12":
		case "PFX":
		case "PEM":
		case "AXX":
		case "EEA":
		case "TC":
		case "KODE":
		case "BPW":
		case "KDB":
		case "KDBX":
			tipo = "seguridad";
			break;
		case "3DV":
		case "AMF":
		case "AWG":
		case "AI":
		case "CGM":
		case "CDR":
		case "CMX":
		case "DP":
		case "DXF":
		case "E2D":
		case "EPS":
		case "FS":
		case "GBR":
		case "DAA":
		case "CIF":
		case "C2D":
		case "ODG":
		case "SVG":
		case "STL":
		case "WRL":
		case "X3D":
		case "SXD":
		case "V2D":
		case "VDOC":
		case "VSD":
		case "VSDX":
		case "VND":
		case "WMF":
		case "EMF":
		case "ART":
		case "XAR":
			tipo = "vector";
			break;
		case "BLP":
		case "BMP":
		case "BTI":
		case "CD5":
		case "CIT":
		case "CPT":
		case "CR2":
		case "CLIP":
		case "CPL":
		case "DDS":
		case "EXIF":
		case "GRF":
		case "ICNS":
		case "ICO":
		case "IFF":
		case "IBM":
		case "ILBM":
		case "JNG":
		case "JPG":
		case "JPEG":
		case "JP2":
		case "JPS":
		case "LBM":
		case "MAX":
		case "MIFF":
		case "MSP":
		case "NITF":
		case "OTB":
		case "PBM":
		case "PC1":
		case "PC2":
		case "PC3":
		case "PCF":
		case "PDN":
		case "PCX":
		case "PGM":
		case "PI1":
		case "PI2":
		case "PI3":
		case "PICT":
		case "PCT":
		case "PNG":
		case "PNM":
		case "PNS":
		case "PPM":
		case "PSB":
		case "PSD":
		case "PDD":
		case "PSP":
		case "PX":
		case "PXM":
		case "PXR":
		case "QFX":
		case "RLE":
		case "SCT":
		case "SGI":
		case "RGB":
		case "INT":
		case "BW":
		case "TGA":
		case "TARGA":
		case "ICB":
		case "VDA":
		case "VST":
		case "PIX":
		case "TIF":
		case "TIFF":
		case "VTF":
		case "XBM":
		case "XCF":
		case "XMP":
		case "ZIF":
			tipo = "fichero imagen";
			break;
		case "4DB":
		case "4DD":
		case "4D":
		case "4DR":
		case "ACCDB":
		case "ACCDE":
		case "ADT":
		case "APR":
		case "BOX":
		case "CHML":
		case "DAF":
		case "DB":
		case "DBF":
		case "DTA":
		case "ESS":
		case "EAP":
		case "FDB":
		case "FP":
		case "FP3":
		case "FP5":
		case "FP7":
		case "GDB":
		case "GTABLE":
		case "KEXI":
		case "KEXIC":
		case "KEXIS":
		case "LDB":
		case "MDA":
		case "MDB":
		case "ADP":
		case "MDE":
		case "MDF":
		case "LDMYDB":
		case "MYI":
		case "NTF":
		case "NV2":
		case "ODB":
		case "ORA":
		case "PCONTACT":
		case "PDB":
		case "PDI":
		case "PDX":
		case "PRC":
		case "SQL":
		case "REC":
		case "REL":
		case "RIN":
		case "SDB":
		case "UDL":
		case "WADATA":
		case "WAINDX":
		case "WAMODEL":
		case "WAJOURNAL":
		case "WDB":
		case "WMDB":
			tipo = "base datos";
			break;
		case "ALIAS":
		case "JNLP":
		case "APPREF-MS":
		case "URL":
		case "WEBLOC":
		case "SYM":
		case "DESKTOP":
		case "LNK":
			tipo = "enlace";
			break;
		case "7Z":
		case "AAPKG":
		case "ALZ":
		case "APPX":
		case "AT3":
		case "BKE":
		case "ARC":
		case "ARJ":
		case "ASS":
		case "B":
		case "BA":
		case "BIG":
		case "BIN":
		case "BJSN":
		case "BKF":
		case "BZ2":
		case "BLD":
		case "CAB":
		case "C4":
		case "CALS":
		case "CLIPFLAIR":
		case "DEB":
		case "DDZ":
		case "DN":
		case "DOE":
		case "ECAB":
		case "EZIP":
		case "ESD":
		case "FLIPCHART":
		case "GBP":
		case "GHO":
		case "GZ":
		case "IPG":
		case "LBR":
		case "LAWRENCE":
		case "LZH":
		case "LZ":
		case "LZO":
		case "LZMA":
		case "LZX":
		case "MBW":
		case "MHTML":
		case "MPQ":
		case "OSK":
		case "NTH":
		case "OAR":
		case "OSZ":
		case "PAK":
		case "PAR":
		case "PAF":
		case "PEA":
		case "PYK":
		case "RAG":
		case "PK3":
		case "PK4":
		case "RAR":
		case "RAGS":
		case "RAX":
		case "RPM":
		case "SB":
		case "SB3":
		case "SEN":
		case "SITX":
		case "SIS":
		case "SISX":
		case "SQ":
		case "SWM":
		case "SZS":
		case "TAR":
		case "UHA":
		case "TB":
		case "TIB":
		case "UUE":
		case "VIV":
		case "VOL":
		case "VSA":
		case "WAX":
		case "WIM":
		case "XZ":
		case "Z":
		case "ZOO":
		case "ZIP":
			tipo = "comprimido";
			break;

		case "A26":
		case "A52":
		case "A78":
		case "LNX":
		case "JAG":
		case "J64":
		case "WBFS":
		case "WAD":
		case "WDF":
		case "GCM":
		case "NDS":
		case "3DS":
		case "CIA":
		case "GB":
		case "GBC":
		case "GBA":
		case "N64":
		case "SGM":
		case "PAL":
		case "PJ":
		case "NES":
		case "FDS":
		case "GG":
		case "SMS":
		case "SG":
		case "SMD":
		case "32X":
			tipo = "juego";
			break;

		case "GSLIDES":
		case "KEY":
		case "NOTA":
		case "OTP":
		case "ODP":
		case "PEZ":
		case "POT":
		case "PPS":
		case "PPT":
		case "PPTX":
		case "PRZ":
		case "SSD":
		case "SHF":
		case "SHOW":
		case "SHW":
		case "SLP":
		case "SSPSS":
		case "STI":
		case "SXI":
		case "THMX":
		case "RELOJ":
			tipo = "presentacion";
			break;

		default:
			tipo = "desconocido";
			break;
		}
		return tipo;
	}

	/**
	 * devuelve la ruta de una imagen relaciondo con la extensión
	 * 
	 * @param tipo {@link String} que contiene la extension de un fichero.
	 * @return {@link String} con la ruta de una imagen relacionada con la extension
	 *         de un fichero.
	 */

	public String rutaImagen(String tipo) {
		return "./src/imagenes/" + tipo + ".gif";
	}

	/**
	 * devuelve un objeto {@link String} para mostrar con pantalla la informacion de
	 * esta clase {@link Ficheros}.
	 * 
	 * @return {@link String} con la información de la clase {@link Ficheros}.
	 */

	@Override
	public String toString() {
		String texto = "";
		for (File fichero : ficheros) {
			if (fichero.isFile()) {
				texto = texto + "fichero\n";
				texto = texto + "\tNombre: " + fichero.getName() + "\n";
				texto = texto + "\tExtension: " + getExtensionFichero(fichero.getPath()) + "\n";
				texto = texto + "\tTipo: " + tipoFichero(fichero.getPath()) + "\n";
				texto = texto + "\tEs Fichero: " + esFichero(fichero) + "\n";
				texto = texto + "\tEs Carpeta: " + esCarpeta(fichero) + "\n\n";
			}
		}
		return texto;
	}

	/**
	 * devuelve una lista {@link ArrayList} conteniendo objetos {@link String} con
	 * el contendio de esta clase {@link Ficheros} que construye una clase
	 * {@link Contenido} de tipo Ficheros, para mostrar en la clase {@link Ventana}.
	 * 
	 * @return {@link ArrayList} que contiene {@link String} para mostrar por la
	 *         clase {@link Ventana}.
	 */

	public ArrayList<String[]> ficherosToContenido() {
		ArrayList<String[]> listaFicherosToContenido = new ArrayList<String[]>();
		for (File fichero : ficheros) {
			if (fichero.isFile()) {
				String[] ficheroToContenido = { "Nombre:  " + fichero.getName(),
						"Peso:  " + String.valueOf(fichero.length()) + "bits", "Tipo Fichero:  " + tipoFichero(fichero.getAbsolutePath()),
						rutaImagen(tipoFichero(fichero.getPath())) };
				listaFicherosToContenido.add(ficheroToContenido);
			} else if (fichero.isDirectory()) {
				String[] ficheroToContenido = { "Nombre:  " + fichero.getName(), "Carpeta", "",
						rutaImagen(tipoFichero("carpeta")) };
				listaFicherosToContenido.add(ficheroToContenido);
			}
		}
		return listaFicherosToContenido;
	}

}
