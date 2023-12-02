package org.murraybridgebunyips.ftc.bunyipslib

enum class StartingPositions {
    RED_LEFT, RED_RIGHT, BLUE_LEFT, BLUE_RIGHT;

    companion object {
        /**
         * Convert StartingPositions into a list of OpModeSelections. Useful in ABOM setOpModes().
         */
        @JvmStatic
        fun use(): List<OpModeSelection> {
            return listOf(
                OpModeSelection(RED_LEFT),
                OpModeSelection(RED_RIGHT),
                OpModeSelection(BLUE_LEFT),
                OpModeSelection(BLUE_RIGHT)
            )
        }
    }
}
