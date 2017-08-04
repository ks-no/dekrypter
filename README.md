# CMS Dekryptering

Et lite program som dekrypterer files kryptert med CMS kryptering

# Intallasjon og bruk

Programmet krever java 8 med unlimited crypto.

* mvn package
* pakk opp target/dekrypter-1.0-dist.zip til egnet mappe
* cd egenet mappe
* java -jar dekrypter.jar \<options\>

Options for dekrypter-jar:
<pre>
-?,--help                         Printer oversikt over mulige
                                  kommandolinjeargumenter.
-d,--delete                       Delete source files after decrypt.
-k,--key <key>                    Private key for dekryptering av filene.
                                  Offentlig nøkkel som tilhører må være
                                  lagt inn i SvarUt.
-kp,--keypassword <keypassword>   Private key password.
-s,--source <source>              source fil eller mappe
-t,--target <target>              mappe som ukrypterte filer lagres i
</pre>

 