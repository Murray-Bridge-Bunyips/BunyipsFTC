package org.firstinspires.ftc.teamcode.common.vision.data

import org.firstinspires.ftc.teamcode.common.vision.TeamProp

/**
 * Utility data structure for Team Prop detections.
 * @author Lucas Bubner, 2023
 */
data class TeamPropData(
    /**
     * Position of the prop in the image.
     */
    val position: TeamProp.Positions
) : VisionData()
