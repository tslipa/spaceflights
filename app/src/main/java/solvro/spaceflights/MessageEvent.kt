package solvro.spaceflights

import solvro.spaceflights.database.Entity

class MessageEvent(val id: Int, val entity: Entity?, val direction: Boolean)