package com.almasb.zeph.entity.character.component;

import com.almasb.ents.AbstractComponent;
import com.almasb.zeph.combat.Skill;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class SkillsComponent extends AbstractComponent {

    protected Skill[] skills;

    /**
     *
     * @return a skill array for this character
     */
    public Skill[] getSkills() {
        return skills;
    }
}
