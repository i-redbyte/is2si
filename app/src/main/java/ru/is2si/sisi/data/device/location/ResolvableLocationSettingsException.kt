package ru.is2si.sisi.data.device.location

import com.google.android.gms.common.api.ResolvableApiException

class ResolvableLocationSettingsException(
        val resolvableApiException: ResolvableApiException
) : RuntimeException()