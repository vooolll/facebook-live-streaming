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

final case class ProfileId(value: String)

sealed trait Profile

final case class EntityAtTextRange(
  id: ProfileId,
  length: Int,
  name: String,
  profile: Profile,
  offset: Int,
  entityType: EntityType
)

sealed trait EntityType

case object UserType extends EntityType
case object PageType extends EntityType
case object EventType extends EntityType
case object GroupType extends EntityType
case object ApplicationType extends EntityType

final case class StoryAttachmentMedia(image: ImageSource)
final case class ImageSource(height: Double, source: String, width: Double)

final case class StoryAttachment(
  description      : String,
  descriptionTags  : List[EntityAtTextRange],
  title            : String,
  typeOfAttachment : String,
  url              : String,
  target           : StoryAttachmentTarget,
  media            : StoryAttachmentMedia)

final case class StoryAttachmentId(value: String)

final case class StoryAttachmentTarget(id: StoryAttachmentId, url: String)

final case class FacebookUserId(value: String)

final case class FacebookUser(id: FacebookUserId) extends Profile

final case class MessageTagId(value: String)

sealed trait TagType

final case class Video()

final case class MessageTags(
  id      : MessageTagId,
  name    : String,
  tagType : TagType,
  offset  : Int,
  length  : Int)
