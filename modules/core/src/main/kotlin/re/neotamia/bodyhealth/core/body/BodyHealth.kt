package re.neotamia.bodyhealth.core.body

import re.neotamia.bodyhealth.core.body.config.Config
import java.util.logging.Logger

class BodyHealth {
    private val config: Config
    private val logger: Logger

    constructor(config: Config, logger: Logger) {
        this.config = config
        this.logger = logger
    }
}
