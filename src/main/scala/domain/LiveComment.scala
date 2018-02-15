package domain

import java.time.Instant

final case class LiveCommentId(value: String)

final case class LiveComment(
  id          : LiveCommentId,
  canRemove   : Boolean,
  canHide     : Boolean,
  canLike     : Boolean,
  createdTime : Instant,
  from        : FacebookUser,
  message     : String,
  messageTags : MessageTags,
  video       : Video)

final case class StoryAttachmentId(value: String)

final case class StoryAttachment(id: StoryAttachmentId)

final case class FacebookUserId(value: String)

final case class FacebookUser(id: FacebookUserId)

final case class MessageTagId(value: String)

sealed trait TagType

final case class Video()

final case class MessageTags(
  id      : MessageTagId,
  name    : String,
  tagType : TagType,
  offset  : Int,
  length  : Int)
