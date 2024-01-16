package com.bbva.utilitieslib.iso

import android.util.Log
import com.bbva.utilitieslib.iso.data.Version
import com.pagatodoholdings.utilitieslib.interfaces.IEmpty

private const val DEFAULT_FILENAME = ""
private const val DEFAULT_MAXCOUNT = 0
private const val DEFAULT_VERSION_MINOR = -1
abstract class AbstractUnpacker<U,S>(val initVersion: Version = Version(), val fileName: String = DEFAULT_FILENAME,
                                     val maxCount: Int = DEFAULT_MAXCOUNT): IUnpacker<U, S>, IEmpty {

    var unpack: Boolean =  false
        protected set

    private val list: MutableList<U> = mutableListOf()

    override fun isEmpty() = (initVersion.isEmpty() && fileName.isEmpty()
              && maxCount == DEFAULT_MAXCOUNT && list.isEmpty())
    override fun getCounter() = list.size

    fun search(value: S): Int {
        validateUnpack()
        for (index in 0..getCounter())
            if (specificSearch(index, value))
                return index

            return -1
    }

    fun unpacker(fileName: String){
        if(!unpack){
            //val jsonFiel = resources.getFileToString(R.raw.)
            //Validar como se va a tomar el archivo de configuración para el ISO
            //Revisar su estara versionado
            unpack = true
        }else
            Log.i("ISO", "Already Unpacker")

    }

    fun getItem( index: Int ): U{
        validateUnpack()
        return list[index]
    }
    protected fun checkVersion(version: Version){
        if( initVersion.compareTo(version) == DEFAULT_VERSION_MINOR )
            throw UnsupportedOperationException("initVersion [$initVersion] > version [$version] ")
    }

    protected fun validateUnpack(){
        if( !unpack )
            throw ExceptionInInitializerError("Unpack [$fileName]")
    }
}