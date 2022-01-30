package com.valkryst.V2DSprite.event;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Frame;
import com.valkryst.V2DSprite.type.AnimationEventType;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.EventObject;

public class AnimationEvent extends EventObject implements Serializable {
	@Serial
	private static final long serialVersionUID = 1;

	/** The type of event. */
	@Getter private final AnimationEventType type;

	/** The {@link Frame} related to this event. */
	@Getter private final Frame frame;

	/** Index of the {@code frame} */
	@Getter private final int index;

	/**
	 * Constructs a new instance of {@code AnimationEvent}.
	 *
	 * @param source The animation which triggered this event.
	 *
	 * @param type
	 * 			An enum indicating the type of event. For information on
	 * 			allowable values, see {@link AnimationEventType}.
	 *
	 * @param frame The {@link Frame} related to this event.
	 *
	 * @param index Index of the {@code frame}.
	 */
	public AnimationEvent(final Animation source, final @NonNull AnimationEventType type, final @NonNull Frame frame, final int index) {
		super(source);
		this.type = type;
		this.frame = frame;
		this.index = index;
	}
}
