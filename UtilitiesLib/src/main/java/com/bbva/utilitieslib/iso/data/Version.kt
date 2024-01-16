package com.bbva.utilitieslib.iso.data

import com.pagatodoholdings.utilitieslib.interfaces.IEmpty

private const val DEFAULT_MAJOR = 0
private const val DEFAULT_MINOR = 0
private const val DEFAULT_BUILD = 0
private const val DEFAULT_REVISION = 0
data class Version( var major: Int = DEFAULT_MAJOR, var minor: Int = DEFAULT_MINOR,
                    var build: Int = DEFAULT_BUILD, var revision: Int = DEFAULT_REVISION): IEmpty{

    override fun isEmpty() = (major == DEFAULT_MAJOR && minor == DEFAULT_MINOR
            && build == DEFAULT_BUILD && revision == DEFAULT_REVISION )

    override fun toString() = "$major.$minor.$build.$revision"

    enum class ELevel{
        MAJOR,
        MAJOR_MINOR,
        MAJ_MIN_BUILD,
        MAJ_MIN_BUILD_REV
    }
    fun toString(level: ELevel) =
        when(level){
            Version.ELevel.MAJOR -> "$major"
            Version.ELevel.MAJOR_MINOR -> "$major.$minor"
            Version.ELevel.MAJ_MIN_BUILD ->"$major.$minor.$build"
            Version.ELevel.MAJ_MIN_BUILD_REV ->"$major.$minor.$build.$revision"
        }

    fun compareTo(value: Version): Int {
        if( major != value.major )
            return if( major > value.major ) 1 else -1

        if( minor != value.minor )
            return if( minor > value.minor ) 1 else -1

        if( build != value.build )
            return if( build > value.build ) 1 else -1

        if( revision != value.revision )
            return if( revision > value.revision ) 1 else -1

        return 0;
    }

    companion object{
        fun parser(value: String): Version{
            if(value.isNullOrEmpty())
                throw NullPointerException("Value Null or Empty")

            return Version().apply {
                val length = value.length
                if (length > 20)
                    throw  IllegalArgumentException("String [$value] length [$length] > 20")


                var index = 0
                var position = 0
                val newValue = "$value."

                while (index < length){
                    val at = newValue.indexOf('.', index)
                    if (at < 0)
                        throw IllegalArgumentException("Version [$value] wrong")

                    val calc = at - index
                    val number = newValue.substring(index, calc).toInt()

                    when(position){
                        0 -> major = number
                        1 -> minor = number
                        2 -> build = number
                        3 -> revision = number
                        else -> throw IllegalArgumentException("Version [$value] wrong")
                    }
                    index = at + 1
                    ++position
                }
            }
        }
    }

}
